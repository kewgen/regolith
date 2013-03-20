package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerMoveWarriorAllyAnswer;
import com.geargames.regolith.serializers.answers.ServerMoveWarriorEnemyAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerAllyCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.BattleMapHelper;

import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * User: mkutuzov
 * Date: 21.07.12
 */
public class ServerMoveWarriorRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerMoveWarriorRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        RegolithConfiguration regolithConfiguration = BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration();

        byte warriorAllianceNumber = from.get();
        int warriorGroupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);

        BattleGroup warriorGroup = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, warriorAllianceNumber, warriorGroupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(warriorGroup, warriorId);

        int x = SimpleDeserializer.deserializeShort(from);
        int y = SimpleDeserializer.deserializeShort(from);
        BattleMapHelper.clearRoutes(warrior, warrior.getX(), warrior.getY());
        BattleMapHelper.clearViewAround(warrior);
        final Map<BattleAlliance, List<Pair>> alliancesTraces = new HashMap<BattleAlliance, List<Pair>>();

        final BattleAlliance warriorAlliance = warrior.getBattleGroup().getAlliance();
        final Battle battle = serverBattle.getBattle();

        for (BattleAlliance alliance : alliancesTraces.keySet()) {
            if (alliance != warriorAlliance) {
                alliancesTraces.put(alliance, new LinkedList<Pair>());
            }
        }

        MoveOneStepListener listener = new MoveOneStepListener() {
            @Override
            public void onStep(Warrior warrior, int x, int y) {
                int nx = warrior.getX() + x;
                int ny = warrior.getY() + y;
                BattleMap map = battle.getMap();
                BattleCell[][] cells = map.getCells();
                for (BattleAlliance alliance : battle.getAlliances()) {
                    if (alliance != warriorAlliance) {
                        Pair pair;
                        if (BattleMapHelper.isVisible(cells[nx][ny], alliance)) {
                            pair = new Pair();
                            pair.setX(nx);
                            pair.setY(ny);
                        } else {
                            pair = null;
                        }
                        alliancesTraces.get(alliance).add(pair);
                    }
                }
            }
        };
        ServerAllyCollection enemies = (ServerAllyCollection) WarriorHelper.move(warrior, x, y, listener, regolithConfiguration.getBattleConfiguration());
        List<MessageToClient> answers = new ArrayList<MessageToClient>(battle.getBattleType().getAllianceAmount());

        int i = 0;
        for (BattleAlliance alliance : battle.getAlliances()) {
            Collection<SocketChannel> recipients = BattleServiceRequestUtils.getRecipients(serverBattle.getAlliances().get(i++));
            if (alliance != warriorAlliance) {
                answers.add(new BattleMessageToClient(recipients, new ServerMoveWarriorEnemyAnswer(to, warrior, alliancesTraces.get(alliance)).serialize()));
            } else {
                answers.add(new BattleMessageToClient(recipients, new ServerMoveWarriorAllyAnswer(to, warrior, enemies).serialize()));
            }
        }

        return answers;
    }
}

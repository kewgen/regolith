package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.map.TerminalPair;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerMoveAllyAnswer;
import com.geargames.regolith.serializers.answers.ServerMoveEnemyAnswer;
import com.geargames.regolith.serializers.answers.ServerMoveWarriorAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerHumanElementCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.HumanElement;
import com.geargames.regolith.units.map.MoveOneStepListener;

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
        List<MessageToClient> answers = null;
        if (warriorGroup != null) {

            Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(warriorGroup, warriorId);
            if (warrior != null) {
                ServerHumanElementCollection units = serverBattle.getHumanElements();
                HumanElement unit = BattleMapHelper.getHumanElementByHuman(units, warrior);

                int x = SimpleDeserializer.deserializeShort(from);
                int y = SimpleDeserializer.deserializeShort(from);
                final Map<BattleAlliance, List<Pair>> alliancesTraces = new HashMap<BattleAlliance, List<Pair>>();
                final BattleAlliance warriorAlliance = warrior.getBattleGroup().getAlliance();
                final Battle battle = serverBattle.getBattle();
                final BattleCell[][] cells = battle.getMap().getCells();

                for (BattleAlliance alliance : alliancesTraces.keySet()) {
                    if (alliance != warriorAlliance) {
                        alliancesTraces.put(alliance, new LinkedList<Pair>());
                    }
                }

                MoveOneStepListener listener = new MoveOneStepListener() {
                    @Override
                    public void onStep(HumanElement unit, int x, int y) {
                        int nx = unit.getCellX() + x;
                        int ny = unit.getCellY() + y;
                        for (BattleAlliance alliance : battle.getAlliances()) {
                            List<Pair> pairs = alliancesTraces.get(alliance);
                            if (pairs != null) {
                                Pair pair;
                                if (BattleMapHelper.isVisible(cells[nx][ny], alliance)) {
                                    pair = new Pair();
                                    pair.setX(nx);
                                    pair.setY(ny);
                                    pairs.add(pair);
                                } else {
                                    if (pairs.size() > 0) {
                                        Pair last = ((LinkedList<Pair>) alliancesTraces.get(alliance)).getLast();
                                        if (last == null || last instanceof TerminalPair) {
                                            pair = null;
                                        } else {
                                            pair = new TerminalPair();
                                            pair.setX(nx);
                                            pair.setY(ny);
                                        }
                                        pairs.add(pair);
                                    }
                                }
                            }
                        }
                    }
                };
                BattleMapHelper.clearRoutes(cells, unit, unit.getCellX(), unit.getCellY(), regolithConfiguration.getBattleConfiguration());
                regolithConfiguration.getBattleConfiguration().getRouter().route(unit, regolithConfiguration.getBattleConfiguration());
                ServerHumanElementCollection enemies = (ServerHumanElementCollection) WarriorHelper.move(cells, unit, x, y, listener, regolithConfiguration.getBattleConfiguration());
                answers = new ArrayList<MessageToClient>(battle.getBattleType().getAllianceAmount());

                int i = 0;
                for (BattleAlliance alliance : battle.getAlliances()) {
                    Collection<SocketChannel> recipients = BattleServiceRequestUtils.getRecipients(serverBattle.getAlliances().get(i++));
                    if (alliance != warriorAlliance) {
                        List<Pair> pairs = alliancesTraces.get(alliance);
                        if (pairs != null && pairs.size() > 0) {
                            answers.add(new BattleMessageToClient(recipients, new ServerMoveEnemyAnswer(to, unit, pairs).serialize()));
                        }
                    } else {
                        List<SocketChannel> recipient = BattleServiceRequestUtils.singleRecipientByClient(client);
                        recipients.removeAll(recipient);
                        answers.add(new BattleMessageToClient(recipient, ServerMoveWarriorAnswer.answerSuccess(to, unit, enemies).serialize()));
                        answers.add(new BattleMessageToClient(recipients, new ServerMoveAllyAnswer(to, unit, enemies).serialize()));
                    }
                }
            } else {
                answers = new ArrayList<MessageToClient>(1);
                answers.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client), ServerMoveWarriorAnswer.answerFailure(to).serialize()));
            }
        } else {
            answers = new ArrayList<MessageToClient>(1);
            answers.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client), ServerMoveWarriorAnswer.answerFailure(to).serialize()));
        }
        return answers;
    }
}

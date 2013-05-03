package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.serializers.answers.ServerShootAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerHumanElementCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.HumanElement;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * User: mkutuzov
 * Date: 23.07.12
 */
public class ServerShootRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerShootRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte hunterAllianceNumber = from.get();
        int hunterGroupId = SimpleDeserializer.deserializeInt(from);
        int hunterId = SimpleDeserializer.deserializeInt(from);
        byte victimAllianceNumber = from.get();
        int victimGroupId = SimpleDeserializer.deserializeInt(from);
        int victimId = SimpleDeserializer.deserializeInt(from);

        BattleGroup hunterGroup = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, hunterAllianceNumber, hunterGroupId);
        Warrior hunter = BattleServiceRequestUtils.getWarriorFromGroup(hunterGroup, hunterId);
        BattleGroup victimGroup = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, victimAllianceNumber, victimGroupId);
        Warrior victim = BattleServiceRequestUtils.getWarriorFromGroup(victimGroup, victimId);

        ServerHumanElementCollection units = serverBattle.getHumanElements();
        HumanElement hunterElement = BattleMapHelper.getHumanElementByHuman(units, hunter);
        HumanElement victimElement = BattleMapHelper.getHumanElementByHuman(units, victim);
        boolean accurately = SimpleDeserializer.deserializeBoolean(from);
        FightHelper.shoot(hunterElement, victimElement, accurately);

        List<MessageToClient> messages = new LinkedList<MessageToClient>();
        Battle battle = serverBattle.getBattle();
        BattleCell[][] cells = battle.getMap().getCells();

        byte[][] answers = new byte[3][];

        for (BattleAlliance alliance : battle.getAlliances()) {
            Warrior aHunter = null;
            Warrior aVictim = null;

            if (BattleMapHelper.isVisible(cells[hunterElement.getCellX()][hunterElement.getCellY()], alliance)) {
                aHunter = hunter;
            }

            if (BattleMapHelper.isVisible(cells[victimElement.getCellX()][victimElement.getCellY()], alliance)) {
                aVictim = victim;
            }

            if (aVictim != aHunter) {
                List<SocketChannel> recipients = BattleServiceRequestUtils.getRecipients(serverBattle.getAlliances().get(alliance.getNumber()));
                int i;
                if (aHunter != null && aVictim != null) {
                    if (answers[0] == null) {
                        answers[0] = new ServerShootAnswer(to, aHunter, aVictim).serialize();
                    }
                    i = 0;
                } else if (aHunter != null) {
                    if (answers[1] == null) {
                        answers[1] = new ServerShootAnswer(to, aHunter, aVictim).serialize();
                    }
                    i = 1;
                } else {
                    if (answers[2] == null) {
                        answers[2] = new ServerShootAnswer(to, aHunter, aVictim).serialize();
                    }
                    i = 2;
                }
                messages.add(new BattleMessageToClient(recipients, answers[i]));
            }
        }

        return messages;
    }
}

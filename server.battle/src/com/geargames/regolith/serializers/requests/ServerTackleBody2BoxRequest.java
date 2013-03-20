package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerTackleBody2BoxAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.tackle.StateTackle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 17.08.12
 * Time: 15:50
 */
public class ServerTackleBody2BoxRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerTackleBody2BoxRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short xBox = SimpleDeserializer.deserializeShort(from);
        short yBox = SimpleDeserializer.deserializeShort(from);
        int tackleId = SimpleDeserializer.deserializeInt(from);

        BattleCell[][] cells = serverBattle.getBattle().getMap().getCells();

        Element element = cells[xBox][yBox].getElement();

        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        StateTackle stateTackle = null;
        Warrior warrior = null;
        Box box = null;
        if (element != null && element instanceof Box) {
            box = (Box) element;

            BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
            warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

            if (warrior.getHeadArmor().getId() == tackleId) {
                stateTackle = warrior.getHeadArmor();
            } else if (warrior.getTorsoArmor().getId() == tackleId) {
                stateTackle = warrior.getTorsoArmor();
            } else if (warrior.getLegsArmor().getId() == tackleId) {
                stateTackle = warrior.getLegsArmor();
            } else if (warrior.getWeapon().getId() == tackleId) {
                stateTackle = warrior.getWeapon();
            }

        }

        if (stateTackle != null) {
            box.getTackles().add(stateTackle);

            Set<Client> clients = new HashSet<Client>();
            clients.addAll(serverBattle.getClients());
            clients.remove(client);

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                    new ServerTackleBody2BoxAnswer(to, stateTackle, warrior, xBox, yBox).serialize()));

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerSuccess(to, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BOX).serialize()));
        } else {
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BOX).serialize()));

        }
        return messages;
    }
}

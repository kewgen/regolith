package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerBox2WarriorAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.BattleCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mkutuzov
 * Date: 25.07.12
 */
public abstract class ServerBox2WarriorRequest extends ServerRequest {
    private short type;
    private ServerBattle serverBattle;

    public ServerBattle getServerBattle() {
        return serverBattle;
    }

    public ServerBox2WarriorRequest(short type, ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
        this.type = type;
    }

    protected abstract boolean putElement(int elementId, Box box, Warrior warrior) throws RegolithException;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short x = SimpleDeserializer.deserializeShort(from);
        short y = SimpleDeserializer.deserializeShort(from);
        int elementId = SimpleDeserializer.deserializeInt(from);

        BattleCell[][] cells = serverBattle.getBattle().getMap().getCells();
        Element element = cells[x][y].getElement();
        if (element != null && element instanceof Box) {
            Box box = (Box) element;

            BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
            Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

            ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(2);
            if (putElement(elementId, box, warrior)) {
                Set<Client> clients = new HashSet<Client>();
                clients.addAll(serverBattle.getClients());
                clients.remove(client);

                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerSuccess(to, type).serialize()));

                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                        new ServerBox2WarriorAnswer(to, type, x, y, element).serialize()));
            } else {
                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerFailure(to, type).serialize()));
            }
            return messages;
        } else {
            throw new RegolithException("There is no box on the map [" + x + ":" + y + "]");
        }
    }
}

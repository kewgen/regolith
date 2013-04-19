package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerBox2GroundAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 26.09.12
 * Time: 14:23
 */
public abstract class ServerBox2GroundRequest extends ServerRequest {
    private ServerBattle serverBattle;
    private short type;

    public ServerBox2GroundRequest(ServerBattle serverBattle, short type) {
        this.serverBattle = serverBattle;
        this.type = type;
    }

    protected abstract CellElement putOut(int elementId, Box box, Warrior warrior, short x, short y) throws RegolithException;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short xBox = SimpleDeserializer.deserializeShort(from);
        short yBox = SimpleDeserializer.deserializeShort(from);
        short xGround = SimpleDeserializer.deserializeShort(from);
        short yGround = SimpleDeserializer.deserializeShort(from);
        int elementId = SimpleDeserializer.deserializeInt(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);
        BattleCell[][] cells = serverBattle.getBattle().getMap().getCells();

        List<MessageToClient> messages = new ArrayList<MessageToClient>(2);

        if (BattleMapHelper.ableToPut(warrior, cells, xGround, yGround) && BattleMapHelper.isNear(warrior, xBox, yBox)
                && cells[xBox][yBox].getElement() != null && cells[xBox][yBox].getElement() instanceof Box) {
            Box box = (Box)cells[xBox][yBox].getElement();
            CellElement element = putOut(elementId, box, warrior, xGround, yGround);
            if (element != null){
                Set<Client> clients = new HashSet<Client>();
                clients.addAll(serverBattle.getClients());
                clients.remove(client);

                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerSuccess(to, type).serialize()));

                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                        new ServerBox2GroundAnswer(to, type, xBox, yBox, element, xGround, yGround).serialize()));
            } else {
                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerFailure(to, type).serialize()));
            }
        } else {
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, type).serialize()));
        }

        return messages;
    }
}

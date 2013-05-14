package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerTackleBag2BoxAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.tackle.StateTackle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 18.08.12
 * Time: 23:07
 */
public class ServerTackleBag2BoxRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerTackleBag2BoxRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short x = SimpleDeserializer.deserializeShort(from);
        short y = SimpleDeserializer.deserializeShort(from);
        short number = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        BattleCell[][] cells = serverBattle.getBattle().getMap().getCells();
        BattleCell cell = cells[x][y];
        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>();

        CellElement cellElement = BattleMapHelper.getElementByType(cell, CellElementTypes.BOX);

        if (BattleMapHelper.isNear(warrior, x, y) && cellElement != null && cellElement instanceof Box) {
            Box box = (Box) cellElement;
            StateTackle tackle = WarriorHelper.putOutOfBag(warrior, number);
            box.getTackles().add(tackle);

            Set<Client> clients = new HashSet<Client>();
            clients.addAll(serverBattle.getClients());
            clients.remove(client);

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                    new ServerTackleBag2BoxAnswer(to, warrior, tackle, x, y).serialize()));

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerSuccess(to, Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_BOX).serialize()));

        } else {
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_BOX).serialize()));
        }
        return messages;
    }
}

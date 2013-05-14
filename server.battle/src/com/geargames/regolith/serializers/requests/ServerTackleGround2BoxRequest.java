package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleCellHelper;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerTackleGround2BoxAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.map.CellElementLayers;
import com.geargames.regolith.units.tackle.StateTackle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 16.08.12
 * Time: 23:50
 */
public class ServerTackleGround2BoxRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerTackleGround2BoxRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short xGround = SimpleDeserializer.deserializeShort(from);
        short yGround = SimpleDeserializer.deserializeShort(from);
        short xBox = SimpleDeserializer.deserializeShort(from);
        short yBox = SimpleDeserializer.deserializeShort(from);

        BattleCell[][] cells = serverBattle.getBattle().getMap().getCells();

        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(1);

        CellElement boxElement = BattleCellHelper.getElementFromLayer(cells[xBox][yBox], CellElementLayers.DYNAMIC);
        CellElement tackleElement = BattleCellHelper.getElementFromLayer(cells[xGround][yGround], CellElementLayers.TACKLE);
        if (tackleElement != null && tackleElement instanceof StateTackle && boxElement != null && boxElement instanceof Box) {
            BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
            Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

            if (BattleMapHelper.isNear(warrior, xGround, yGround) && BattleMapHelper.isNear(warrior, xBox, yBox)) {
                Box box = (Box) boxElement;
                StateTackle tackle = (StateTackle) tackleElement;
                box.getTackles().add(tackle);

                Set<Client> clients = new HashSet<Client>();
                clients.addAll(serverBattle.getClients());
                clients.remove(client);

                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                        new ServerTackleGround2BoxAnswer(to, xBox, yBox, xGround, yGround).serialize()));

                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerSuccess(to, Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BOX).serialize()));

            } else {
                messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BOX).serialize()));
            }
        } else {
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BOX).serialize()));
        }
        return messages;
    }
}

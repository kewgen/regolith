package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerGround2BoxAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 28.09.12
 * Time: 16:18
 */
public abstract class ServerGround2BoxRequest extends ServerRequest {
    private ServerBattle serverBattle;
    private short type;

    public ServerGround2BoxRequest(ServerBattle serverBattle, short type) {
        this.serverBattle = serverBattle;
        this.type = type;
    }

    protected abstract void moveGround2Box(BattleCell cell, Box to) throws RegolithException;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short groundX = SimpleDeserializer.deserializeShort(from);
        short groundY = SimpleDeserializer.deserializeShort(from);
        short boxX = SimpleDeserializer.deserializeShort(from);
        short boxY = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        BattleMap map = serverBattle.getBattle().getMap();
        BattleCell[][] cells = map.getCells();
        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(2);

        BattleConfiguration battleConfiguration = BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration().getBattleConfiguration();
        int action = battleConfiguration.getActionFees().getPickupTackle() * 2;

        if (BattleMapHelper.ableToPeek(warrior, cells, groundX, groundY)) {
            if (warrior.getActionScore() >= action) {
                CellElement boxElement = BattleMapHelper.getElementByType(cells[boxX][boxY], CellElementTypes.BOX);
                if (BattleMapHelper.isNear(warrior, boxX, boxY) && boxElement != null && boxElement instanceof Box) {
                    moveGround2Box(cells[groundX][groundY], (Box) boxElement);

                    Set<Client> clients = new HashSet<Client>();
                    clients.addAll(serverBattle.getClients());
                    clients.remove(client);

                    messages.add(new BattleMessageToClient(
                            BattleServiceRequestUtils.singleRecipientByClient(client),
                            ServerConfirmationAnswer.answerSuccess(to, type).serialize()
                    ));

                    messages.add(new BattleMessageToClient(
                            BattleServiceRequestUtils.getRecipients(clients),
                            new ServerGround2BoxAnswer(to, groundX, groundY, boxX, boxY, type).serialize()
                    ));
                } else {
                    messages.add(new BattleMessageToClient(
                            BattleServiceRequestUtils.singleRecipientByClient(client),
                            ServerConfirmationAnswer.answerFailure(to, type).serialize()
                    ));
                }
            } else {
                messages.add(new BattleMessageToClient(
                        BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerFailure(to, type).serialize()
                ));
            }
        } else {
            messages.add(new BattleMessageToClient(
                    BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, type).serialize()
            ));
        }

        return messages;
    }
}

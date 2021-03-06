package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerMedikitGround2BoxAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.tackle.Medikit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 25.09.12
 * Time: 15:39
 */
public class ServerMedikitGround2BoxRequest extends ServerRequest {
    private ServerBattle serverBattle;

    public ServerMedikitGround2BoxRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);

        short fromX = SimpleDeserializer.deserializeShort(from);
        short fromY = SimpleDeserializer.deserializeShort(from);
        short boxX = SimpleDeserializer.deserializeShort(from);
        short boxY = SimpleDeserializer.deserializeShort(from);
        short position = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        BattleMap map = warrior.getBattleGroup().getAlliance().getBattle().getMap();
        BattleCell[][] cells = map.getCells();

        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);

        RegolithConfiguration regolithConfiguration = BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration();

        if (BattleMapHelper.ableToPeek(warrior, cells, fromX, fromY)) {
            if (BattleMapHelper.isNear(warrior, boxX, boxY)) {
                CellElement element = BattleMapHelper.getElementByType(map.getCells()[fromX][fromY], CellElementTypes.MAGAZINE);

                if (element instanceof Medikit) {
                    Medikit medikit = (Medikit) element;

                    CellElement elementBox = BattleMapHelper.getElementByType(map.getCells()[boxX][boxY], CellElementTypes.BOX);
                    if (elementBox instanceof Box) {
                        map.getCells()[fromX][fromY].removeElement(element);
                        WarriorHelper.payForPickOrPut(warrior, regolithConfiguration.getBattleConfiguration());
                        Box box = (Box) elementBox;
                        box.getMedikits().insert(medikit, position);

                        Set<Client> clients = new HashSet<Client>();
                        clients.addAll(serverBattle.getClients());
                        clients.remove(client);

                        messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                                ServerConfirmationAnswer.answerSuccess(to, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BOX).serialize()));

                        messages.add(new BattleMessageToClient(
                                BattleServiceRequestUtils.getRecipients(clients),
                                new ServerMedikitGround2BoxAnswer(to, fromX, fromY, boxX, boxY, position).serialize()
                        ));
                    } else {
                        messages.add(new BattleMessageToClient(
                                BattleServiceRequestUtils.singleRecipientByClient(client)
                                , ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BOX).serialize()));
                    }
                } else {
                    messages.add(new BattleMessageToClient(
                            BattleServiceRequestUtils.singleRecipientByClient(client)
                            , ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BOX).serialize()));
                }
            } else {
                messages.add(new BattleMessageToClient(
                        BattleServiceRequestUtils.singleRecipientByClient(client)
                        , ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BOX).serialize()));
            }
        } else {
            messages.add(new BattleMessageToClient(
                    BattleServiceRequestUtils.singleRecipientByClient(client)
                    , ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BOX).serialize()));
        }

        return messages;
    }
}

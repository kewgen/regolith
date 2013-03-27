package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerGround2GroundAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 25.09.12
 * Time: 13:09
 */
public class ServerGround2GroundRequest extends ServerRequest {
    private ServerBattle serverBattle;
    private short type;

    public ServerGround2GroundRequest(ServerBattle serverBattle, short type) {
        this.serverBattle = serverBattle;
        this.type = type;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);

        short fromX = SimpleDeserializer.deserializeShort(from);
        short fromY = SimpleDeserializer.deserializeShort(from);
        short toX = SimpleDeserializer.deserializeShort(from);
        short toY = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        BattleMap map = warrior.getBattleGroup().getAlliance().getBattle().getMap();
        BattleCell[][] cells = map.getCells();

        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(1);

        if (BattleMapHelper.ableToPeek(warrior, cells, fromX, fromY)) {
            if (BattleMapHelper.ableToPut(warrior, cells, toX, toY)) {
                BattleMapHelper.putIn(BattleMapHelper.putOut(map, fromX, fromY), map, toX, toY);

                Set<Client> clients = new HashSet<Client>();
                clients.addAll(serverBattle.getClients());
                clients.remove(client);

                messages.add(new BattleMessageToClient(
                        BattleServiceRequestUtils.singleRecipientByClient(client),
                        ServerConfirmationAnswer.answerSuccess(to, type).serialize()
                ));

                messages.add(new BattleMessageToClient(
                        BattleServiceRequestUtils.getRecipients(clients)
                        ,new ServerGround2GroundAnswer(to, fromX, fromY, toX, toY, type).serialize()
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

        return messages;
    }
}

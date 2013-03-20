package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerGround2WarriorAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.map.BattleMapHelper;
import com.geargames.regolith.units.tackle.Medikit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 20.08.12
 * Time: 20:00
 */
public class ServerMedikitGround2BagRequest extends ServerRequest {
    private ServerBattle serverBattle;


    public ServerMedikitGround2BagRequest(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        short x = SimpleDeserializer.deserializeShort(from);
        short y = SimpleDeserializer.deserializeShort(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        if (warrior == null) {
            throw new RegolithException("Warrior was not found");
        }

        Medikit medikit = BattleMapHelper.peekMedikit(warrior, group.getAlliance().getBattle().getMap().getCells(), x, y);

        ArrayList<MessageToClient> messages = new ArrayList<MessageToClient>(2);
        if (medikit != null && WarriorHelper.putInToBag(warrior, medikit, 1,
                BattleServiceConfigurationFactory.getConfiguration().getRegolithConfiguration().getBaseConfiguration()) == 1) {
            Set<Client> clients = new HashSet<Client>();
            clients.addAll(serverBattle.getClients());
            clients.remove(client);

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerSuccess(to, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BAG).serialize()));

            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                    new ServerGround2WarriorAnswer(to, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BAG, x, y, warrior).serialize()));
        } else {
            messages.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BAG).serialize()));
        }
        return messages;
    }
}

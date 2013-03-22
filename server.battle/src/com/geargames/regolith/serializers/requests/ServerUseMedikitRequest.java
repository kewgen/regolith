package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ServerUseMedikitAnswer;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.tackle.Medikit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mikhail v. kutuzov
 * Date: 01.10.12
 * Time: 12:52
 */
public class ServerUseMedikitRequest extends ServerRequest {
    private ServerBattle serverBattle;
    private RegolithConfiguration regolithConfiguration;

    public ServerUseMedikitRequest(ServerBattle serverBattle, RegolithConfiguration regolithConfiguration) {
        this.serverBattle = serverBattle;
        this.regolithConfiguration = regolithConfiguration;
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        int medikitId = SimpleDeserializer.deserializeInt(from);

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        Medikit medikit = BaseConfigurationHelper.findMedikitById(medikitId, regolithConfiguration.getBaseConfiguration());


        List<MessageToClient> messageToClients = new ArrayList<MessageToClient>(2);

        if (WarriorHelper.useMedikit(warrior, medikit, regolithConfiguration)) {
            messageToClients.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerSuccess(to, Packets.USE_MEDIKIT).serialize()));

            Set<Client> clients = new HashSet<Client>();
            clients.addAll(serverBattle.getClients());
            clients.remove(client);

            messageToClients.add(new BattleMessageToClient(BattleServiceRequestUtils.getRecipients(clients),
                    new ServerUseMedikitAnswer(to, warrior).serialize()));
        } else {
            messageToClients.add(new BattleMessageToClient(BattleServiceRequestUtils.singleRecipientByClient(client),
                    ServerConfirmationAnswer.answerFailure(to, Packets.USE_MEDIKIT).serialize()));
        }

        return messageToClients;
    }
}

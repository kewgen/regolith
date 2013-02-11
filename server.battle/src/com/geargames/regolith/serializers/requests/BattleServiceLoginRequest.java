package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.BattleServiceLoginAnswer;
import com.geargames.regolith.serializers.answers.BattleServiceNewClientLogin;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.state.ClientAtBattle;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 14.08.12
 * Time: 16:28
 */
public class BattleServiceLoginRequest extends ServerRequest {

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {

        BattleServiceConfiguration configuration = BattleServiceConfigurationFactory.getConfiguration();
        BattleServiceContext context = configuration.getContext();
        ServerBattle serverBattle = context.getServerBattleById(SimpleDeserializer.deserializeInt(from));
        serverBattle.getClients().add((BattleClient)client);

        int allianceId = SimpleDeserializer.deserializeInt(from);
        int accountId = SimpleDeserializer.deserializeInt(from);
        Account account = null;
        for (BattleGroup tmp : serverBattle.getGroups()) {
            if (tmp.getAccount().getId() == accountId) {
                account = tmp.getAccount();
                break;
            }
        }
        if (account == null) {
            throw new RegolithException("An account is wrong");
        }
        String name = SimpleDeserializer.deserializeString(from);
        String password = SimpleDeserializer.deserializeString(from);
        SerializedMessage message;
        List<MessageToClient> messages = new ArrayList<MessageToClient>(3);
        if (account.getName().equals(name) && account.getPassword().equals(password)) {
            BattleAlliance alliance = BattleHelper.findAlliance(serverBattle.getBattle(), allianceId);
            serverBattle.getAlliances().get(alliance.getNumber()).add((BattleClient)client);
            Collection<SocketChannel> recipients = BattleServiceRequestUtils.getRecipients(serverBattle.getClients());
            serverBattle.getClients().add((BattleClient)client);
            Battle battle = serverBattle.getBattle();
            BattleGroup battleGroup = null;
            for (BattleGroup group : ((ServerBattleGroupCollection) (battle.getAlliances()[alliance.getNumber()].getAllies())).getBattleGroups()) {
                if (account.getId() == group.getAccount().getId()) {
                    battleGroup = group;
                    serverBattle.getReadyGroups().add(group);
                    break;
                }
            }
            List<SocketChannel> recipient = new ArrayList<SocketChannel>(1);
            recipient.add(client.getChannel());
            message = BattleServiceLoginAnswer.answerSuccess(to, battle, serverBattle.getReadyGroups());
            messages.add(new BattleMessageToClient(recipient, message.serialize()));

            if (serverBattle.getClients().size() == serverBattle.getGroups().size()) {
                configuration.getBattleSchedulerService().add(serverBattle);
            } else {
                message = new BattleServiceNewClientLogin(to, battle, battleGroup);
                messages.add(new BattleMessageToClient(recipients, message.serialize()));
                client.setState(new ClientAtBattle(serverBattle));
            }
        } else {
            List<SocketChannel> recipient = new ArrayList<SocketChannel>(1);
            recipient.add(client.getChannel());

            message = BattleServiceLoginAnswer.answerFailure(to, serverBattle.getBattle());
            messages.add(new BattleMessageToClient(recipient, message.serialize()));
        }

        return messages;
    }
}


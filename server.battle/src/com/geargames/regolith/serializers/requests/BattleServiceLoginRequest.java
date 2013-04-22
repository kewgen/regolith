package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.helpers.BattleHelper;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.serializers.answers.BattleServiceLoginAnswer;
import com.geargames.regolith.serializers.answers.BattleServiceNewClientLogin;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.state.ClientActivationAwaiting;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 14.08.12
 * Time: 16:28
 */
public class BattleServiceLoginRequest extends ServerRequest {

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        SerializedMessage message;
        List<MessageToClient> messages = new ArrayList<MessageToClient>(3);

        BattleServiceConfiguration configuration = BattleServiceConfigurationFactory.getConfiguration();
        BattleServiceContext context = configuration.getContext();
        ServerBattle serverBattle = context.getServerBattleById(SimpleDeserializer.deserializeInt(from));
        if (serverBattle != null) {
            ((BattleClient) client).setServerBattle(serverBattle);
            serverBattle.getClients().add((BattleClient) client);

            int allianceId = SimpleDeserializer.deserializeInt(from);
            int accountId = SimpleDeserializer.deserializeInt(from);
            BattleGroup group = null;
            Account account = null;
            for (BattleGroup tmp : serverBattle.getGroups()) {
                if (tmp.getAccount().getId() == accountId) {
                    account = tmp.getAccount();
                    group = tmp;
                    break;
                }
            }

            if (account != null) {
                String name = SimpleDeserializer.deserializeString(from);
                String password = SimpleDeserializer.deserializeString(from);
                if (account.getName().equals(name) && account.getPassword().equals(password)) {
                    client.setAccount(account);
                    Battle battle = serverBattle.getBattle();
                    BattleAlliance alliance = BattleHelper.findAllianceById(battle, allianceId);
                    serverBattle.getAlliances().get(alliance.getNumber()).add((BattleClient) client);
                    Collection<SocketChannel> recipients = BattleServiceRequestUtils.getRecipients(serverBattle.getClients());
                    serverBattle.getClients().add((BattleClient) client);
                    BattleGroup battleGroup = null;
                    serverBattle.getReadyGroups().add(group);

                    List<SocketChannel> recipient = new ArrayList<SocketChannel>(1);
                    recipient.add(client.getChannel());
                    message = BattleServiceLoginAnswer.answerSuccess(to, battle, serverBattle.getReadyGroups());
                    messages.add(new BattleMessageToClient(recipient, message.serialize()));
                    client.setState(new ClientActivationAwaiting());
                    SecurityOperationManager securityOperationManager = new SecurityOperationManager();
                    account.setSecurity(securityOperationManager);
                    securityOperationManager.setAccount(client.getAccount());
                    message = new BattleServiceNewClientLogin(to, battle, battleGroup);
                    messages.add(new BattleMessageToClient(recipients, message.serialize()));

                    if (serverBattle.getClients().size() == serverBattle.getGroups().size()) {
                        configuration.getBattleSchedulerService().add(messages);
                        configuration.getBattleSchedulerService().add(serverBattle);
                        return new ArrayList<MessageToClient>();
                    }
                } else {
                    List<SocketChannel> recipient = new ArrayList<SocketChannel>(1);
                    recipient.add(client.getChannel());

                    message = BattleServiceLoginAnswer.answerFailure(to, serverBattle.getBattle());
                    messages.add(new BattleMessageToClient(recipient, message.serialize()));
                }
            } else {
                List<SocketChannel> recipient = new ArrayList<SocketChannel>(1);
                recipient.add(client.getChannel());

                message = BattleServiceLoginAnswer.answerFailure(to, serverBattle.getBattle());
                messages.add(new BattleMessageToClient(recipient, message.serialize()));
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


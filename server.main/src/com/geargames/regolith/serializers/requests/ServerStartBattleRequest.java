package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerTrainingBattleCreationManager;
import com.geargames.regolith.serializers.answers.ServerStartBattleAnswer;
import com.geargames.regolith.serializers.answers.ServerStopListenAnswer;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.MainServerRequestUtils;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.service.*;
import com.geargames.regolith.service.remote.BattleServiceDescriptor;
import com.geargames.regolith.service.remote.BattleServiceFunctions;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;

import java.net.MalformedURLException;
import java.nio.channels.SocketChannel;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public class ServerStartBattleRequest extends ServerRequest {
    private BattleManagerContext battleManagerContext;
    private ServerTrainingBattleCreationManager battleCreationManager;
    private ServerContext serverContext;

    public ServerStartBattleRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.battleManagerContext = configuration.getServerContext().getBattleManagerContext();
        this.battleCreationManager = configuration.getBattleCreationManager();
        this.serverContext = configuration.getServerContext();
    }

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        Battle battle;
        List<MessageToClient> messages = new LinkedList<MessageToClient>();
        if (battleManagerContext.getCreatedBattles().get(client.getAccount()) != null) {
            List<SocketChannel> recipients;
            battle = battleCreationManager.startBattle(client.getAccount());
            if (battle != null) {

                BattleServiceDescriptor battleServiceDescriptor = ServerContextHelper.getWastingBattleServiceAddress(serverContext);
                if (battleServiceDescriptor == null) {
                    throw new RegolithException();
                }
                BattleServiceFunctions battleService = null;
                try {
                    battleService = (BattleServiceFunctions)Naming.lookup("rmi://" + battleServiceDescriptor.getHost() + "/" + battleServiceDescriptor.getName());
                    battleService.register(battle);
                } catch (NotBoundException e) {
                    throw new RegolithException(e);
                } catch (MalformedURLException e) {
                    throw new RegolithException(e);
                } catch (RemoteException e) {
                    throw new RegolithException(e);
                }

                Set<Client> clients = MainServerRequestUtils.getBattleClients(battle, serverContext);
                for (Client groupClient : clients) {
                    Account account = groupClient.getAccount();
                    recipients = new ArrayList<SocketChannel>(1);
                    SocketChannel channel = serverContext.getChannel(account);
                    recipients.add(channel);
                    serverContext.removeChannel(account);
                    serverContext.removeClient(channel);
                    messages.add(new MainMessageToClient(recipients, ServerStartBattleAnswer.answerSuccess(to, battle, account, battleServiceDescriptor).serialize()));
                }

                List<SocketChannel> listeners = MainServerRequestUtils.getPassiveListenerChannels(clients, serverContext, battle);
                if (listeners.size() > 0) {
                    messages.add(new MainMessageToClient(listeners, new ServerStopListenAnswer(to, battle).serialize()));
                }

                battleManagerContext.getCreatedBattles().remove(client.getAccount());
                battleManagerContext.getBattlesById().remove(battle.getId());
                battleManagerContext.getBattleListeners().remove(battle);
                battleManagerContext.getCompleteGroups().remove(battle);

            } else {
                recipients = new ArrayList<SocketChannel>(1);
                recipients.add(serverContext.getChannel(client.getAccount()));
                messages.add(new MainMessageToClient(recipients, ServerStartBattleAnswer.answerFailure(to).serialize()));
            }
        } else {
            throw new RegolithException();
        }

        return messages;
    }

}

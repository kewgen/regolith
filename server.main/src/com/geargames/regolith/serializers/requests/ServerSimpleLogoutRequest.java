package com.geargames.regolith.serializers.requests;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.ServerContext;
import com.geargames.regolith.service.states.ClientNotLoggedIn;
import com.geargames.regolith.units.Account;

import java.nio.channels.SocketChannel;

/**
 * User: abarakov
 * Date: 16.04.13
 */
// ServerLogoutFromBaseRequest
public class ServerSimpleLogoutRequest {

    public void clientRequest(MicroByteBuffer from, Client client) {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        ServerContext serverContext = serverConfiguration.getServerContext();

        Account activeAccount = serverConfiguration.getServerContext().getActiveAccountById(client.getAccount().getId());
        if (activeAccount != null) {
            if (client.getAccount() != activeAccount) {
                Debug.error("ServerSimpleLogoutRequest.clientRequest(): client.getAccount() != activeAccount");
            }
            // Клиент все еще подключен => разлогиним его
            serverConfiguration.getCommonManager().logout(activeAccount);
            client.setState(new ClientNotLoggedIn());
            serverContext.removeChannel(client.getAccount());
            serverContext.removeClient(client.getChannel());
        } else {
            // Клиент послал логаут, будучи не залогиненым
            //todo: как на это реагировать?
        }
    }

}

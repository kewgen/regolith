package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.serializers.answers.FirstServerLoginAnswer;
import com.geargames.regolith.serializers.answers.ServerLoginAnswer;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.states.ClientAtBase;
import com.geargames.regolith.service.states.ClientAtBaseWarriorsMarket;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public class ServerLoginRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        Login login = new Login();
        login.setName(SimpleDeserializer.deserializeString(from));
        login.setPassword(SimpleDeserializer.deserializeString(from));
        Account account = serverConfiguration.getCommonManager().login(login);
        if (account != null) {
            if (serverConfiguration.getServerContext().getActiveAccountById(account.getId()) != null) {
                return ServerLoginAnswer.answerFailure(writeBuffer);
            } else {
                serverConfiguration.getServerContext().addChannel(account, client.getChannel());
                client.setAccount(account);
                if (account.getWarriors().size() > 0) {
                    client.setState(new ClientAtBase());
                    return ServerLoginAnswer.answerSuccess(serverConfiguration, account, writeBuffer);
                } else {
                    client.setState(new ClientAtBaseWarriorsMarket());
                    return new FirstServerLoginAnswer(serverConfiguration, account, writeBuffer);
                }
            }
        } else {
            return ServerLoginAnswer.answerFailure(writeBuffer);
        }

    }
}

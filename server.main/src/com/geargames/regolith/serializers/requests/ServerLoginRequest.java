package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ErrorCodes;
import com.geargames.regolith.serializers.answers.ServerFirstLoginAnswer;
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
            Account activeAccount = serverConfiguration.getServerContext().getActiveAccountById(account.getId());
            if (activeAccount != null) {
                // Клиент с таким же логином уже подключился
                return ServerLoginAnswer.answerFailure(writeBuffer, ErrorCodes.LOGON_USER_IS_ALREADY_LOGGED);
            } else {
                serverConfiguration.getServerContext().addChannel(account, client.getChannel());
                client.setAccount(account);
                if (account.getWarriors().size() > 0) {
                    client.setState(new ClientAtBase());
                    return ServerLoginAnswer.answerSuccess(serverConfiguration, account, writeBuffer);
                } else {
                    client.setState(new ClientAtBaseWarriorsMarket());
                    return new ServerFirstLoginAnswer(serverConfiguration, account, writeBuffer);
                }
            }
        } else {
            // Недопустимая пара логин/пароль.
            return ServerLoginAnswer.answerFailure(writeBuffer, ErrorCodes.LOGON_INVALID_USERNAME_OR_PASSWORD);
        }
    }

}

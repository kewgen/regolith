package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.serializers.requests.CheckForNameRequest;
import com.geargames.regolith.serializers.requests.CreateLoginRequest;
import com.geargames.regolith.serializers.requests.LoginRequest;
import com.geargames.regolith.serializers.requests.LogoutRequest;
import com.geargames.regolith.units.Login;


/**
 * @author Mikhail_Kutuzov
 *         created: 22.05.12  18:33
 */
public class ClientCommonManager {
    private ClientConfiguration configuration;

    public ClientCommonManager(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public ClientDeferredAnswer login(Login login) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.LOGIN);
        ClientLoginAnswer clientLoginAnswer = new ClientLoginAnswer(configuration);
        messageLock.setMessage(clientLoginAnswer);
        configuration.getNetwork().sendMessage(new LoginRequest(configuration, login));

        return new ClientDeferredAnswer(clientLoginAnswer);
    }

    public void logout(Login login) {
        configuration.getNetwork().sendMessage(new LogoutRequest(configuration,login.getName()));
    }

    public ClientDeferredAnswer checkForName(String login) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.CHECK_FOR_NAME);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new CheckForNameRequest(configuration, login));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }

    public ClientDeferredAnswer create(Login account) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.CLIENT_REGISTRATION);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new CreateLoginRequest(configuration, account));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }
}

package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.serializers.requests.CheckForNameRequest;
import com.geargames.regolith.serializers.requests.CreateAccountRequest;
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

    /**
     * Послать сообщение о входе пользователем в существующий аккаунт, т.е. залогиниться.
     * @param login
     * @return
     */
    public ClientDeferredAnswer login(Login login) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.LOGIN);
        ClientLoginAnswer clientLoginAnswer = new ClientLoginAnswer(configuration);
        messageLock.setMessage(clientLoginAnswer);
        configuration.getNetwork().sendMessage(new LoginRequest(configuration, login));

        return new ClientDeferredAnswer(clientLoginAnswer);
    }

    /**
     * Послать сообщение о выходе пользователем из своего аккаунта, т.е. разлогинить аккаунт.
     * @param login
     */
    public void logout(Login login) {
        configuration.getNetwork().sendMessage(new LogoutRequest(configuration, login.getName()));
    }

    /**
     * Послать сообщение для проверки того, что имя аккаунта (пользователя) свободно, иными словами его можно
     * использовать при создании нового аккаунта.
     */
    public ClientDeferredAnswer checkForName(String login) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.CHECK_FOR_NAME);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new CheckForNameRequest(configuration, login));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }

    /**
     * Послать сообщение о создании нового аккаунта.
     */
    public ClientDeferredAnswer create(Login account) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.CLIENT_REGISTRATION);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new CreateAccountRequest(configuration, account));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }
}

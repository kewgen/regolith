package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.serializers.requests.ClientCheckForNameRequest;
import com.geargames.regolith.serializers.requests.ClientCreateAccountRequest;
import com.geargames.regolith.serializers.requests.ClientLoginRequest;
import com.geargames.regolith.serializers.requests.ClientLogoutRequest;
import com.geargames.regolith.units.Login;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 22.05.12
 */
public class ClientCommonManager {
    private ClientConfiguration configuration;
    private ClientLoginAnswer loginAnswer;
    private ClientConfirmationAnswer confirmationAnswer;

    public ClientCommonManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        loginAnswer = new ClientLoginAnswer();
        confirmationAnswer = new ClientConfirmationAnswer();
    }

    /**
     * Послать сообщение-запрос о входе пользователем в существующий аккаунт, т.е. залогиниться.
     * @param login
     * @return
     */
    public ClientLoginAnswer login(Login login) throws Exception {
        configuration.getNetwork().sendSynchronousMessage(new ClientLoginRequest(configuration, login), loginAnswer, 100);
        return loginAnswer;
    }

    /**
     * Послать сообщение-запрос о выходе пользователем из своего аккаунта, т.е. разлогинить аккаунт.
     */
    public void logout() {
        configuration.getNetwork().sendMessage(new ClientLogoutRequest(configuration));
    }

    /**
     * Послать сообщение-запрос для проверки того, что имя аккаунта (пользователя) "свободно", иными словами его можно
     * использовать при создании нового аккаунта.
     */
    public ClientConfirmationAnswer checkForName(String login) throws Exception {
        configuration.getNetwork().sendSynchronousMessage(new ClientCheckForNameRequest(configuration, login), confirmationAnswer, 100);
        return confirmationAnswer;
    }

    /**
     * Послать сообщение-запрос о создании нового аккаунта.
     */
    public ClientConfirmationAnswer create(Login account) throws Exception {
        configuration.getNetwork().sendSynchronousMessage(new ClientCreateAccountRequest(configuration, account), confirmationAnswer, 100);
        return confirmationAnswer;
    }

}

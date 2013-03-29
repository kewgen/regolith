package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.serializers.requests.CheckForNameRequest;
import com.geargames.regolith.serializers.requests.CreateAccountRequest;
import com.geargames.regolith.serializers.requests.LoginRequest;
import com.geargames.regolith.serializers.requests.LogoutRequest;
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
        configuration.getNetwork().sendSynchronousMessage(new LoginRequest(configuration, login), loginAnswer, 100);
        return loginAnswer;
    }

    /**
     * Послать сообщение-запрос о выходе пользователем из своего аккаунта, т.е. разлогинить аккаунт.
     * @param login
     */
    public void logout(Login login) {
        configuration.getNetwork().sendMessage(new LogoutRequest(configuration, login.getName()));
    }

    /**
     * Послать сообщение-запрос для проверки того, что имя аккаунта (пользователя) "свободно", иными словами его можно
     * использовать при создании нового аккаунта.
     */
    public ClientConfirmationAnswer checkForName(String login) throws Exception {
        configuration.getNetwork().sendSynchronousMessage(new CheckForNameRequest(configuration, login), confirmationAnswer, 100);
        return confirmationAnswer;
    }

    /**
     * Послать сообщение-запрос о создании нового аккаунта.
     */
    public ClientConfirmationAnswer create(Login account) throws Exception {
        configuration.getNetwork().sendSynchronousMessage(new CreateAccountRequest(configuration, account), confirmationAnswer, 100);
        return confirmationAnswer;
    }

}

package com.geargames.regolith;

import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.managers.ClientCommonManager;
import com.geargames.regolith.managers.ClientDeferredAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.units.Login;
import junit.framework.Assert;

/**
 * User: abarakov
 * Date: 19.03.13
 */
public class ClientLoginHelper {

    public static final String ACCOUNT_NAME_DEFAULT     = "автор";
    public static final String ACCOUNT_PASSWORD_DEFAULT = "секрет";

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        return answer.retrieve(1000);
    }

    public static ClientLoginAnswer clientLogon(String accountName, String accountPassword, boolean createIfNotExist) {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();
//        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        Login login = new Login();
        login.setName(accountName);
        login.setPassword(accountPassword);

        System.out.println("Is checking for a login name");

        ClientDeferredAnswer answer = commonManager.checkForName(accountName);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        if (createIfNotExist) {
            if (confirm.isConfirm()) {
                System.out.println("Trying to create an account");

                answer = commonManager.create(login);
                Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

                confirm = (ClientConfirmationAnswer) answer.getAnswer();
                Assert.assertTrue("Could not create the account", confirm.isConfirm());
            }
        } else {
            // Аккаунт создаваться не будет, он должен существовать
            Assert.assertFalse("Cannot login to account", confirm.isConfirm());
        }

        System.out.println("Going to login");

        answer = commonManager.login(login);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientLoginAnswer loginAnswer = (ClientLoginAnswer) answer.getAnswer();
        Assert.assertNull("Cannot login to account: " + loginAnswer.getError(), loginAnswer.getError());

        return loginAnswer;
    }

}
package com.geargames.regolith;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Date: 18.11.12
 * Time: 21:47
 */
public class MenuTest {
    private static SimpleService service;


    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        return answer.retrieve(200);
    }

    public static void main(String[] arg) throws Exception {
        try {
            service = MainServiceManager.runMainService();
            client();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            service.stopService();
        }
    }

    public static void client() throws Exception {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());
        Login login = new Login();
        login.setName("АВТОР");
        login.setPassword("СЕКРЕТ");
        System.out.println("is checking for a login name");
        ClientDeferredAnswer answer = commonManager.checkForName(login.getName());

        if (!waitForAnswer(answer)) {
            return;
        }

        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        if (confirm.isConfirm()) {
            System.out.println("trying to create an account");
            answer = commonManager.create(login);
            if (!waitForAnswer(answer)) {
                return;
            }

            confirm = (ClientConfirmationAnswer) answer.getAnswer();
            if (!confirm.isConfirm()) {
                System.out.println("could not create an account");
                return;
            }
        }

        System.out.println("going to log in");

        answer = commonManager.login(login);
        if (!waitForAnswer(answer)) {
            return;
        }

        ClientLoginAnswer loginAnswer = (ClientLoginAnswer) answer.getAnswer();
        if (loginAnswer.getError() != null) {
            System.err.println(loginAnswer.getError());
            return;
        }

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());
        clientConfiguration.setAccount(account);

        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        if (clientConfiguration.getBaseWarriors() != null) {
            Warrior[] warriors = clientConfiguration.getBaseWarriors();
            byte amount = clientConfiguration.getBaseConfiguration().getInitWarriorsAmount();
            if (warriors.length < amount) {
                System.out.println("An amount of available warriors is not enough");
                return;
            }
            Warrior[] initWarriors = new Warrior[amount];
            for (int i = 0; i < amount; i++) {
                warriors[i].setName(warriors[i].getName() + "i");
                initWarriors[i] = warriors[i];
            }
            System.out.println("We are trying to hire warriors");
            answer = baseWarriorMarketManager.hireWarrior(initWarriors);
            if (!waitForAnswer(answer)) {
                return;
            }

            ClientJoinBaseWarriorsAnswer clientJoinBaseWarriorsAnswer = (ClientJoinBaseWarriorsAnswer) answer.getAnswer();
            ClientWarriorCollection clientWarriorCollection = new ClientWarriorCollection(new Vector());
            if (clientJoinBaseWarriorsAnswer.isSuccess()) {
                for (Warrior warrior : clientJoinBaseWarriorsAnswer.getWarriors()) {
                    clientWarriorCollection.add(warrior);
                }
                account.setWarriors(clientWarriorCollection);
            } else {
                System.out.println("could not get a set base warriors");
                return;
            }
        }

        RegolithMain.main(null);


    }

}

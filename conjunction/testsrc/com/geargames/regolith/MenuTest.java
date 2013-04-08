package com.geargames.regolith;

import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;

/**
 * User: mikhail v. kutuzov, abarakov
 * Date: 18.11.12
 * Time: 21:47
 */
public class MenuTest {
    private static SimpleService service;

    private static String ACCOUNT_NAME_DEFAULT     = "АВТОР";
    private static String ACCOUNT_PASSWORD_DEFAULT = "СЕКРЕТ";

    public static void main(String[] args) {
        try {
            service = MainServiceManager.runMainService();
            client((args.length == 0 ? ACCOUNT_NAME_DEFAULT : args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.stopService();
        }
    }

    public static void client(String accountName) throws Exception {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon(accountName, ACCOUNT_PASSWORD_DEFAULT, true);

        Account account = loginAnswer.getAccount();
        System.out.println("Account id = " + account.getId());

        System.out.println("Configuring the client...");
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());
        clientConfiguration.setAccount(account);

        ClientTestHelper.hireWarriorForClient(account);

        RegolithMain.main(null);
    }

}

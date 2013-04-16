package com.geargames.regolith;

import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 18.11.12
 * Time: 21:47
 */
public class MenuTest {

    private static String ACCOUNT_NAME_DEFAULT     = "АВТОР";
    private static String ACCOUNT_PASSWORD_DEFAULT = "СЕКРЕТ";

    public static void main(String[] args)throws Exception{
        client((args.length == 0 ? ACCOUNT_NAME_DEFAULT : args[0]));
    }

    public static void client(String accountName) throws Exception {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        Login login = new Login();
        login.setName(accountName);
        login.setPassword(ACCOUNT_PASSWORD_DEFAULT);
        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon(login, true);

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

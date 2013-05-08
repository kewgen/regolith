package com.geargames.regolith;

import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.units.Login;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 18.11.12
 */
public class MenuTest {

    private static String ACCOUNT_NAME_DEFAULT = "АВТОР";
    private static String ACCOUNT_PASSWORD_DEFAULT = "СЕКРЕТ";

    public static void main(String[] args) throws Exception {
        client((args.length == 0 ? ACCOUNT_NAME_DEFAULT : args[0]));
    }

    public static void client(String accountName) throws Exception {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        Login login = new Login();
        login.setName(accountName);
        login.setPassword(ACCOUNT_PASSWORD_DEFAULT);
        clientConfiguration.setLogin(login);

        RegolithMain.main(null);
    }

}

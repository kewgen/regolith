package com.geargames.regolith;

import com.geargames.common.network.Network;
import com.geargames.regolith.managers.ClientCommonManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import org.junit.*;

/**
 * User: mkutuzov
 * Date: 14.06.12
 */
public class SimpleServiceTest {
    private static SimpleService service;

    @BeforeClass
    public static void service() throws Exception  {
        service = MainServiceManager.runMainService();
    }

    @Test
    public void client() throws Exception {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        Network network = clientConfiguration.getNetwork();
        ClientCommonManager managerInterface = clientConfiguration.getCommonManager();
        clientConfiguration.setCommonManager(managerInterface);
        network.connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientConfirmationAnswer message = managerInterface.checkForName("супер пупс с горы1");
        try {
            message.deSerialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(message.isConfirm());


        Login login = new Login();
        login.setName("супер пупс с горы1");
        login.setPassword("супер");

        message = managerInterface.create(login);
        Assert.assertTrue(message.isConfirm());


        ClientLoginAnswer loginAnswer = managerInterface.login(login);;
        Assert.assertNull(loginAnswer.getError());

        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());
        Account account = loginAnswer.getAccount();

        Assert.assertEquals(account.getName(), login.getName());
        managerInterface.logout(login);
    }

    @AfterClass
    public static void stop(){
        service.stopService();
    }
}

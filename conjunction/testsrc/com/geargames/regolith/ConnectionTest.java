package com.geargames.regolith;

import com.geargames.common.network.Network;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.helpers.BattleTypeHelper;
import com.geargames.regolith.managers.ClientBaseManager;
import com.geargames.regolith.managers.ClientBattleCreationManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.managers.ClientCommonManager;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.BattleMap;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * User: abarakov
 * Date: 16.04.13
 */
public class ConnectionTest {
    private static SimpleService service;

    private static final int WAINTING = 2000; // 200 сек

    // Ожидаем асинхронного сообщения и возвращаем true, если сообщение получено.
    private static boolean waitForAsyncAnswer(ClientDeSerializedMessage answer, short msgType, int attemptCount) {
        int i = 0;
        try {
            while (! ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(answer, msgType)) {
                if (i++ >= attemptCount) {
                    return false;
                }
                Manager.pause(100);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @BeforeClass
    public static void service() throws Exception {
        service = MainServiceManager.runMainService();
    }

    @AfterClass
    public static void stop() {
        service.stopService();
    }

    @Test
    public void client() throws Exception {
        ConsoleMainHelper.appInitialize();

        Login login = new Login();
        login.setName("clientA");
        login.setPassword("секрет");

        // -------------------------------------------------------------------------------------------------------------

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        Network network = clientConfiguration.getNetwork();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("Connection to the server...");
        network.connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        System.out.println("Disconnecting from the server...");
        network.disconnect();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("Connection to the server...");
        network.connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon(login, true);
        System.out.println("Configuring the client...");
        Account selfAccount = loginAnswer.getAccount();
        System.out.println("Account id = " + selfAccount.getId());
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());
        clientConfiguration.setAccount(selfAccount);

        System.out.println("Going to logout");
        commonManager.logout();
        clientConfiguration.setBaseConfiguration(null);
        clientConfiguration.setBaseWarriors(null);
        clientConfiguration.setAccount(null);

        System.out.println("Disconnecting from the server...");
        network.disconnect();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("Connection to the server...");
        network.connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        loginAnswer = ClientTestHelper.clientLogon(login, true);
        System.out.println("Configuring the client...");
        selfAccount = loginAnswer.getAccount();
        System.out.println("Account id = " + selfAccount.getId());
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());
        clientConfiguration.setAccount(selfAccount);

        System.out.println("Going to logout");
        commonManager.logout();
        clientConfiguration.setBaseConfiguration(null);
        clientConfiguration.setBaseWarriors(null);
        clientConfiguration.setAccount(null);

        System.out.println("Disconnecting from the server...");
        network.disconnect();

        // -------------------------------------------------------------------------------------------------------------

//        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
//        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
//        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
////        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();
//
//        ClientTestHelper.hireWarriorForClient(selfAccount);
//
//        System.out.println("The client go to the battle market...");
//        ClientConfirmationAnswer confirm = baseManager.goBattleMarket();
//        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());
//
//        System.out.println("Find the appropriate type of battle...");
//        BattleType battleType = BaseConfigurationHelper.findBattleTypeByArgs(2, 1, 1,
//                ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
//
//        System.out.println("Browsing maps...");
//        ClientBattleMapAnswer battleMap = battleMarketManager.browseRandomBattleMap(battleType);
//        BattleMap map = battleMap.getBattleMap();
//        Assert.assertNotNull("There are no maps", map);
//        Manager.pause(300);
//        ClientTestHelper.checkAsyncMessages();
//
//        BattleType bt = BattleTypeHelper.findBattleTypeById(battleType.getId(), map.getPossibleBattleTypes());
//        Assert.assertNotNull("Map does not support the type of battle '1:1x1'", bt);

        // -------------------------------------------------------------------------------------------------------------


        // -------------------------------------------------------------------------------------------------------------

        System.out.println("The test was completed successfully.");
    }

}

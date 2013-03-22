package com.geargames.regolith.creation_connection_startBattleTest;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.*;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.managers.*;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.map.BattleMap;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 27.06.12
 * Time: 14:51
 */
public class BattleCreationTest {
    private static SimpleService service;

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        return answer.retrieve(1000);
    }

    // Ожидаем асинхронного сообщения до 5 сек и возвращаем true, если сообщение получено.
    private static boolean waitForAsyncAnswer(ClientDeSerializedMessage answer, short msgType) {
        int i = 0;
        while (! ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(answer, msgType)) {
            if (i++ >= 1000) {
                return false;
            }
            Manager.pause(100);
        }
        return true;
    }

    @BeforeClass
    public static void service() throws Exception {
        service = MainServiceManager.runMainService();
    }

    @Test
    public void client() throws com.geargames.regolith.RegolithException, BrokenBarrierException, InterruptedException {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("clientA", "секрет", true);

        System.out.println("Configuring the client");

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
//        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        ClientTestHelper.hireWarriorForClient(account);

        System.out.println("Lets go to the battle market");

        ClientDeferredAnswer answer = baseManager.goBattleManager();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue(confirm.isConfirm());

        System.out.println("Browsing maps");

        answer = battleMarketManager.browseBattleMaps();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientBrowseBattleMapsAnswer browseMaps = (ClientBrowseBattleMapsAnswer) answer.getAnswer();
        BattleMap[] maps = browseMaps.getBattleMaps();
        Assert.assertTrue("there are no maps", maps.length > 0);

        // scenario: #0a
        answer = battleMarketManager.createBattle(maps[0], 0);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientCreateBattleAnswer createBattleAnswer = (ClientCreateBattleAnswer) answer.getAnswer();
        Battle battle = createBattleAnswer.getBattle();
        Assert.assertNotNull("Could not create his own battle.", battle);

        // scenario: #1b
        System.out.println("Listening battle...");
/*
        ClientJoinBattleAnswer clientJoinBattleAnswer = new ClientJoinBattleAnswer(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(clientJoinBattleAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE));
        System.out.println(
                "'Client C' (" + clientJoinBattleAnswer.getBattleGroup().getAccount().getName() +
                ") joined to the alliance (id = " + clientJoinBattleAnswer.getBattleGroup().getAlliance().getId() + ")");

        System.out.println("Listening battle completed");
*/

//        BattleGroup group;
//        BattleAlliance alliance = battle.getAlliances()[0];
//        answer = battleCreationManager.joinToAlliance(alliance, account);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        group = ((ClientJoinBattleAnswer) answer.getAnswer()).getBattleGroup();
//        Assert.assertNotNull("Could not joint to his own battle.", group);
//        Assert.assertTrue(account.getName() + " has no enough warriors for this battle", battle.getBattleType().getGroupSize() <= account.getWarriors().size());

//        Warrior[] warriors = new Warrior[battle.getBattleType().getGroupSize()];
//        for (int i = 0; i < warriors.length; i++) {
//            warriors[i] = account.getWarriors().get(i);
//        }
//
//        answer = battleCreationManager.completeGroup(group, warriors);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        confirm = (ClientConfirmationAnswer) answer.getAnswer();
//        Assert.assertTrue("Could not put a group into his own battle.", confirm.isConfirm());
//
//        answer = battleCreationManager.isReady(group);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        confirm = (ClientConfirmationAnswer) answer.getAnswer();
//        Assert.assertTrue("Have not be able to be ready for his own battle.", confirm.isConfirm());
//
//        ClientStartBattleAnswer clientStartBattleAnswer;
//        while (true) {
//            answer = battleCreationManager.startBattle(account);
//            Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//            clientStartBattleAnswer = (ClientStartBattleAnswer) answer.getAnswer();
//            if (clientStartBattleAnswer.isSuccess()) {
//                break;
//            }
//
//            try {
//                System.out.println("The battle could not be started.");
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                Assert.fail("Could not start a battle and was interrupted.");
//            }
//        }
//        System.out.println("The battle has begun");

//        Network network = clientConfiguration.getNetwork();
//        network.disconnect();
//        network.connect(clientStartBattleAnswer.getHost(), clientStartBattleAnswer.getPort());
//
//        System.out.println("Try to login into the battle");
//
//        ClientBattleServiceManager battleServiceManager = clientConfiguration.getBattleServiceManager();
//        answer = battleServiceManager.login(battle, alliance);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        ClientBattleLoginAnswer battleLoginAnswer = (ClientBattleLoginAnswer) answer.getAnswer();
//        Assert.assertTrue("Could not login into the battle", battleLoginAnswer.isSuccess());
//
//        while (true) {
//            if (ClientHelper.isOurTurn(network, alliance, false)) {
//                break;
//            }
//        }
//
//        System.out.println("...");
//
//        answer = battleServiceManager.move(warriors[0], (short) 10, (short) 10);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        ClientAllyMoveAnswer clientAllyMoveAnswer = (ClientAllyMoveAnswer) answer.getAnswer();
//
//        BattleMapHelper.clearRoutes(warriors[0], warriors[0].getX(), warriors[0].getY());
//        BattleMapHelper.clearViewAround(warriors[0]);
//
//        WarriorHelper.move(warriors[0], clientAllyMoveAnswer.getX(), clientAllyMoveAnswer.getY(), new MoveOneStepListener() {
//                    @Override
//                    public void onStep(Warrior warrior, int x, int y) {
//                    }
//                }, clientConfiguration.getBattleConfiguration());
//
//        Warrior warrior = account.getWarriors().get(1);
//
//        BattleMap map = battle.getMap();
//        for (Warrior warriorA : clientAllyMoveAnswer.getEnemies()) {
//            BattleMapHelper.putIn(warriorA, map, warrior.getX(), warrior.getY());
//        }

        System.out.println("The test was completed successfully.");
    }

    @AfterClass
    public static void stop() {
        service.stopService();
    }

}

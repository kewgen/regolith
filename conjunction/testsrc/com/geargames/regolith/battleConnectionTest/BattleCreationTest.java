package com.geargames.regolith.battleConnectionTest;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.*;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.managers.*;
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

    private static final int FIRST_WAINTING = 1000; // 100 сек
    private static final int NEXT_WAINTING  = 200;  // 20 сек

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        try {
            return answer.retrieve(100);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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

    @Test
    public void client() throws com.geargames.regolith.RegolithException, BrokenBarrierException, InterruptedException {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("clientA", "секрет", true);

        System.out.println("Configuring the client...");

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
//        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        ClientTestHelper.hireWarriorForClient(account);

        System.out.println("Lets go to the battle market...");
        ClientDeferredAnswer answer = baseManager.goBattleManager();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue(confirm.isConfirm());

        System.out.println("Browsing maps...");
        answer = battleMarketManager.browseBattleMaps();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientBrowseBattleMapsAnswer browseMaps = (ClientBrowseBattleMapsAnswer) answer.getAnswer();
        BattleMap[] maps = browseMaps.getBattleMaps();
        Assert.assertTrue("there are no maps", maps.length > 0);

        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #0a ==============================");
        answer = battleMarketManager.createBattle(maps[0], 0);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientCreateBattleAnswer createBattleAnswer = (ClientCreateBattleAnswer) answer.getAnswer();
        Battle battle = createBattleAnswer.getBattle();
        Assert.assertNotNull("Could not create his own battle.", battle);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #1b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, FIRST_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Account accountClientC = joinToBattleAllianceAnswer.getBattleGroup().getAccount();
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #1c ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (Client C)...");
        ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = new ClientEvictAccountFromAllianceAnswer();
        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #2b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #2c ==============================");
        System.out.println("Trying to eviction the client from the battle (by author)...");
        answer = battleCreationManager.evictAccount(joinToBattleAllianceAnswer.getBattleGroup().getAlliance(), accountClientC);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) answer.getAnswer();
//        BattleGroup battleGroup = evictAccountFromAllianceAnswer.getBattleGroup();
        Assert.assertTrue("The client could not be evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #3b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3c ==============================");
        System.out.println("Waiting for a client's readiness for the battle (Client C)...");
        ClientGroupReadyStateAnswer groupReadyStateAnswer = new ClientGroupReadyStateAnswer();
        groupReadyStateAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not establish its readiness",
                waitForAsyncAnswer(groupReadyStateAnswer, Packets.GROUP_IS_READY, NEXT_WAINTING));
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == groupReadyStateAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + groupReadyStateAnswer.getBattleGroup().getAccount().getName() +
                "' established readiness for battle (id = " + groupReadyStateAnswer.getBattleGroup().getAlliance().getBattle().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3d ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (Client C)...");
        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #4b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4c ==============================");
        System.out.println("Waiting for a client's readiness for the battle (Client C)...");
        groupReadyStateAnswer = new ClientGroupReadyStateAnswer();
        groupReadyStateAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not establish its readiness",
                waitForAsyncAnswer(groupReadyStateAnswer, Packets.GROUP_IS_READY, NEXT_WAINTING));
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == groupReadyStateAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + groupReadyStateAnswer.getBattleGroup().getAccount().getName() +
                "' established readiness for battle (id = " + groupReadyStateAnswer.getBattleGroup().getAlliance().getBattle().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4d ==============================");
        System.out.println("Trying to eviction the client from the battle (by author)...");
        answer = battleCreationManager.evictAccount(joinToBattleAllianceAnswer.getBattleGroup().getAlliance(), accountClientC);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("The client could not be evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #5b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300+1000); // + секунда, т.к. в это время выполняются сценарии #5c и #5d у клиентов B и C
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #5e ==============================");
        System.out.println("Trying to cancelation the battle (by author)...");
        answer = battleCreationManager.cancelBattle();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//      ClientCancelBattleAnswer cancelBattleAnswer = (ClientCancelBattleAnswer) answer.getAnswer();
//      Assert.assertTrue("The client could not be evicted from the alliance", cancelBattleAnswer.isSuccess()); //todo: реализовать метод isSuccess
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #6a ==============================");

        //todo: goBattleManager - надо ли?
        System.out.println("Lets go to the battle market...");
        answer = baseManager.goBattleManager();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue(confirm.isConfirm());

        System.out.println("Browsing maps...");
        answer = battleMarketManager.browseBattleMaps();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        browseMaps = (ClientBrowseBattleMapsAnswer) answer.getAnswer();
        maps = browseMaps.getBattleMaps();
        Assert.assertTrue("there are no maps", maps.length > 0);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        answer = battleMarketManager.createBattle(maps[0], 0);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        createBattleAnswer = (ClientCreateBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("Could not create his own battle.", createBattleAnswer.getBattle());
//        Assert.assertTrue("Different references to the battles", battle == createBattleAnswer.getBattle());
        battle = createBattleAnswer.getBattle();
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6d ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6e ==============================");
        System.out.println("Waiting for a client's readiness for the battle (Client C)...");
        groupReadyStateAnswer = new ClientGroupReadyStateAnswer();
        groupReadyStateAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not establish its readiness",
                waitForAsyncAnswer(groupReadyStateAnswer, Packets.GROUP_IS_READY, NEXT_WAINTING));
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client account", accountClientC.getId() == groupReadyStateAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + groupReadyStateAnswer.getBattleGroup().getAccount().getName() +
                "' established readiness for battle (id = " + groupReadyStateAnswer.getBattleGroup().getAlliance().getBattle().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6f ==============================");
        System.out.println("Trying to cancelation the battle (by author)...");
        answer = battleCreationManager.cancelBattle();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//      ClientCancelBattleAnswer cancelBattleAnswer = (ClientCancelBattleAnswer) answer.getAnswer();
//      Assert.assertTrue("The client could not be evicted from the alliance", cancelBattleAnswer.isSuccess()); //todo: реализовать метод isSuccess
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------



        Manager.pause(300+1000); // + секунда, т.к. в это время выполняется сценарий #7g у клиента B




//        System.out.println("Listening battle completed");

//        BattleGroup group;
//        BattleAlliance alliance = battle.getAlliances()[0];
//        answer = battleCreationManager.joinToAlliance(alliance, account);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        group = ((ClientJoinToBattleAllianceAnswer) answer.getAnswer()).getBattleGroup();
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
//        System.out.println("Try to login into the battle...");
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

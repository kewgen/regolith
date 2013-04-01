package com.geargames.regolith.battleConnectionTest;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.*;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.helpers.BattleTypeHelper;
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
 */
public class BattleCreationTest {
    private static SimpleService service;

    private static final int FIRST_WAINTING = 1000; // 100 сек
    private static final int NEXT_WAINTING  = 2000;  // 20 сек
    private static final int BROWSE_CREATED_BATTLES_WAINTING = 200; // 20 сек

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

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("clientA", "секрет", true);

        System.out.println("Configuring the client...");

        Account selfAccount = loginAnswer.getAccount();
        System.out.println("Account id = " + selfAccount.getId());
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());
        clientConfiguration.setAccount(selfAccount);

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
//        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        ClientTestHelper.hireWarriorForClient(selfAccount);

        System.out.println("The client go to the battle market...");
        ClientConfirmationAnswer confirm = baseManager.goBattleManager();
        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        System.out.println("Find the appropriate type of battle...");
        BattleType battleType = BaseConfigurationHelper.findBattleTypeByArgs(2, 1, 1,
                ClientConfigurationFactory.getConfiguration().getBaseConfiguration());

        System.out.println("Browsing maps...");
        ClientBattleMapAnswer battleMap = battleMarketManager.browseRandomBattleMap(battleType);
        BattleMap map = battleMap.getBattleMap();
        Assert.assertNotNull("There are no maps", map);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        BattleType bt = BattleTypeHelper.findBattleTypeById(battleType.getId(), map.getPossibleBattleTypes());
        Assert.assertNotNull("Map does not support the type of battle '1:1x1'", bt);

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #0a ==============================");
        System.out.println("Creating a battle (map id = " + map.getId() + ")...");
        ClientListenToBattleAnswer listenToBattleAnswer = battleMarketManager.createBattle(map, battleType);
        Battle battle = listenToBattleAnswer.getBattle();
        System.out.println("Created battle (battle id=" + battle.getId() + ")");
        //todo: battle.getAuthor() == null
        //todo: battle.getMap() == null
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
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
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
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300 + 1000); // +1 секунда, чтобы клиент C успел выполнить свой сценарий #2b
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #2c ==============================");
        System.out.println("Trying to eviction the client from the battle (by author)...");
        evictAccountFromAllianceAnswer = battleCreationManager.evictAccount(joinToBattleAllianceAnswer.getBattleGroup().getAlliance(), accountClientC);
//        BattleGroup battleGroup = evictAccountFromAllianceAnswer.getBattleGroup();
        Assert.assertTrue("The client could not be evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(800);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #3b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3c ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        ClientCompleteGroupAnswer completeGroupAnswer = new ClientCompleteGroupAnswer();
        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

//        System.out.println("========== scenario: #3d ==============================");
//        System.out.println("Waiting for a client's readiness for the battle (Client C)...");
//        ClientGroupReadyStateAnswer groupReadyStateAnswer = new ClientGroupReadyStateAnswer();
//        groupReadyStateAnswer.setBattle(battle);
//        Assert.assertTrue("'Client C' has not establish its readiness",
//                waitForAsyncAnswer(groupReadyStateAnswer, Packets.GROUP_IS_READY, NEXT_WAINTING));
//        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
//        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == groupReadyStateAnswer.getBattleGroup().getAccount().getId());
//        System.out.println("Client '" + groupReadyStateAnswer.getBattleGroup().getAccount().getName() +
//                "' established readiness for battle (id = " + groupReadyStateAnswer.getBattleGroup().getAlliance().getBattle().getId() + ")");
//        Manager.pause(300);
//        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3d ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (Client C)...");
        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
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
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4c ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300 + 1000); // +1 секунда, чтобы клиент C успел выполнить свой сценарий #4c
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4d ==============================");
        System.out.println("Trying to eviction the client from the battle (by author)...");
        evictAccountFromAllianceAnswer = battleCreationManager.evictAccount(joinToBattleAllianceAnswer.getBattleGroup().getAlliance(), accountClientC);
        Assert.assertTrue("The client could not be evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(800);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #5b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300 + 2500); // +2.5 секунды, т.к. в это время выполняются сценарии #5b, #5c и #5d у клиентов B и C
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #5e ==============================");
        System.out.println("Trying to cancelation the battle (by author)...");
        ClientCancelBattleAnswer cancelBattleAnswer = battleCreationManager.cancelBattle();
        Assert.assertTrue("The client is not able to cancel the battle", cancelBattleAnswer.isSuccess());
        System.out.println("The client canceled battle (battle id = " + battle.getId() + ")");
        Manager.pause(800);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #6a ==============================");

        System.out.println("The client go to the battle market...");
        confirm = baseManager.goBattleManager(); //todo: надо ли?
        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        System.out.println("Browsing maps...");
        battleMap = battleMarketManager.browseRandomBattleMap(battleType);
        map = battleMap.getBattleMap();
        Assert.assertNotNull("There are no maps", map);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("Creating a battle (map id = " + map.getId() + ")...");
        listenToBattleAnswer = battleMarketManager.createBattle(map, battleType);
        Assert.assertNotNull("Could not create his own battle.", listenToBattleAnswer.getBattle());
//        Assert.assertTrue("Different references to the battles", battle == listenToBattleAnswer.getBattle());
        battle = listenToBattleAnswer.getBattle();
        System.out.println("Created battle (battle id=" + battle.getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6d ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING + BROWSE_CREATED_BATTLES_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6e ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300 + 1000); // +1 секунда, чтобы клиент C успел выполнить свой сценарий #6e
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6f ==============================");
        System.out.println("Trying to cancelation the battle (by author)...");
        cancelBattleAnswer = battleCreationManager.cancelBattle();
        Assert.assertTrue("The client is not able to cancel the battle", cancelBattleAnswer.isSuccess());
        System.out.println("The client canceled battle (battle id = " + battle.getId() + ")");
        Manager.pause(800);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #7a ==============================");

        System.out.println("The client go to the battle market...");
        confirm = baseManager.goBattleManager(); //todo: надо ли?
        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        System.out.println("Browsing maps...");
        battleMap = battleMarketManager.browseRandomBattleMap(battleType);
        map = battleMap.getBattleMap();
        Assert.assertNotNull("There are no maps", map);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("Creating a battle (map id = " + map.getId() + ")...");
        listenToBattleAnswer = battleMarketManager.createBattle(map, battleType);
        Assert.assertNotNull("Could not create his own battle.", listenToBattleAnswer.getBattle());
//        Assert.assertTrue("Different references to the battles", battle == listenToBattleAnswer.getBattle());
        battle = listenToBattleAnswer.getBattle();
        System.out.println("Created battle (battle id=" + battle.getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7d ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING + BROWSE_CREATED_BATTLES_WAINTING));
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7e ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300 + 1000); // +1 секунда, чтобы клиент C успел выполнить свой сценарий #7e
        ClientTestHelper.checkAsyncMessages();

        BattleAlliance alliance = ClientTestHelper.getFreeAlliance(battle);
        Assert.assertNotNull("There is no empty battle group for the client", alliance);

        System.out.println("========== scenario: #7f ==============================");
        System.out.println("The author of battle is trying join to an alliance (alliance id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        joinToBattleAllianceAnswer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("'Client A' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        BattleGroup battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Assert.assertNotNull("'Client A' could not join to the alliance", battleGroup);
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == battleGroup.getAccount().getId());
        System.out.println("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (alliance id = " + battleGroup.getAlliance().getId() + ")");
        Manager.pause(300 + 1000); // +1 секунда, т.к. в это время выполняется сценарий #7g у клиента B
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7h ==============================");
        System.out.println("The client is trying to complete the battle group (battle group id = " + battleGroup.getId() + ")");
        int groupSize = battle.getBattleType().getGroupSize();
        Assert.assertTrue("Client '" + selfAccount.getName() + "' does not have enough warriors", groupSize <= selfAccount.getWarriors().size());
        Warrior[] warriors = new Warrior[groupSize];
        for (int i = 0; i < groupSize; i++) {
            warriors[i] = selfAccount.getWarriors().get(i);
        }
        completeGroupAnswer = battleCreationManager.completeGroup(battleGroup, warriors);
        Assert.assertTrue("The client could not complete the battle group (battle group id = " + battleGroup.getId() + ")", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client A'", selfAccount.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(800);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7i ==============================");
        System.out.println("Waiting of disbandment the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not disband the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_DISBAND, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not disband the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' disbanded the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300 + 1000); // +1 секунда, т.к. в это время выполняется сценарий #7i у клиента C
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7j ==============================");
        System.out.println("Trying to fake start a battle (by Author)...");
        ClientStartBattleAnswer startBattleAnswer = battleCreationManager.startBattle(selfAccount);
        Assert.assertFalse("The client should not start the battle", startBattleAnswer.isSuccess());
        Manager.pause(800);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7k ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300 + 3000); // +3 секунды, т.к. в это время выполняются сценарии #7l - #7o у клиентов B и C
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7p ==============================");
        System.out.println("Trying to start a battle (by Author)...");
        startBattleAnswer = battleCreationManager.startBattle(selfAccount);
        Assert.assertTrue("The client can not start the battle", startBattleAnswer.isSuccess());
        Assert.assertNotNull("Battle can not be null", startBattleAnswer.getBattle());
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == startBattleAnswer.getBattle().getAuthor().getId());
        System.out.println("Client '" + startBattleAnswer.getBattle().getAuthor().getName() +
                "' began the battle (battle id = " + startBattleAnswer.getBattle().getId() +
                ", address: " + startBattleAnswer.getHost() + ":" + startBattleAnswer.getPort() + ")");
        Manager.pause(800);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("The test was completed successfully.");
    }

}

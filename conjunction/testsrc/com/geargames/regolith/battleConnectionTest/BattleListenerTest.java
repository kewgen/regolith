package com.geargames.regolith.battleConnectionTest;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.ClientTestHelper;
import com.geargames.regolith.Packets;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Vector;

/**
 * User: abarakov
 * Date: 19.03.13
 */
public class BattleListenerTest {

    private static final int FIRST_WAINTING = 1000; // 100 сек
    private static final int NEXT_WAINTING  = 2000;  // 20 сек
    private static final int BROWSE_CREATED_BATTLES_WAINTING = 200;  // 20 сек

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

    @Test
    public void client() throws Exception {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("clientB", "секрет", true);

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
        ClientConfirmationAnswer confirm = baseManager.goBattleMarket();
        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        System.out.println("Listening created battles...");
        confirm = battleMarketManager.listenToCreatedBattles();
        Assert.assertTrue(confirm.isConfirm());
        Assert.assertTrue("The client can not start listening to created battles", confirm.isConfirm());

        System.out.println("Waiting receiving a list of battles...");
        ObjectManager objectManager = ObjectManager.getInstance();
        Vector battleVector = objectManager.getBattleCollection().getBattles();
        battleVector.clear();
        ClientBrowseBattlesAnswer browseBattlesAnswer = new ClientBrowseBattlesAnswer();
        Assert.assertTrue("'Client C' does not receive a list of battles",
                waitForAsyncAnswer(browseBattlesAnswer, Packets.BROWSE_CREATED_BATTLES, BROWSE_CREATED_BATTLES_WAINTING));
        Assert.assertNotNull("??? The client does not receive a list of battles", browseBattlesAnswer.getAnswers());
        ClientBattleCollection battles = ObjectManager.getInstance().getBattleCollection();
        Assert.assertNotNull("??? The client does not receive a list of battles", battles);
        Assert.assertTrue("Battle available to play should be one (battle count = " + battles.size() + ")", battles.size() == 1);
        Battle battle = battles.get(0);
        clientConfiguration.setBattle(battle);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("Do not listening created battles...");
        confirm = battleMarketManager.doNotListenToCreatedBattles();
        Assert.assertTrue("The client can not stop listening to created battles", confirm.isConfirm());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #0b ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        ClientListenToBattleAnswer listen = battleMarketManager.listenToBattle(battle);
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
//        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #1b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
//        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, FIRST_WAINTING));
        Assert.assertTrue("??? 'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Account accountClientC = joinToBattleAllianceAnswer.getBattleGroup().getAccount();
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #1c ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (Client C)...");
        ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = new ClientEvictAccountFromAllianceAnswer();
//        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #2b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
//        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("??? 'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #2c ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (Client C)...");
        evictAccountFromAllianceAnswer = new ClientEvictAccountFromAllianceAnswer();
//        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #3b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
//        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("??? 'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3c ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        ClientCompleteGroupAnswer completeGroupAnswer = new ClientCompleteGroupAnswer();
//        completeGroupAnswer.setBattle(battle);
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
////        groupReadyStateAnswer.setBattle(battle);
//        Assert.assertTrue("'Client C' has not establish its readiness",
//                waitForAsyncAnswer(groupReadyStateAnswer, Packets.GROUP_IS_READY, NEXT_WAINTING));
//        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
//        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == groupReadyStateAnswer.getBattleGroup().getAccount().getId());
//        System.out.println("Client '" + groupReadyStateAnswer.getBattleGroup().getAccount().getName() +
//                "' established readiness for battle (id = " + groupReadyStateAnswer.getBattleGroup().getAlliance().getBattle().getId() + ")");
//        Manager.pause(300);
//        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3d ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (Client C)...");
        evictAccountFromAllianceAnswer = new ClientEvictAccountFromAllianceAnswer();
//        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #4b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
//        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("??? 'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4c ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
//        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4d ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (Client C)...");
        evictAccountFromAllianceAnswer = new ClientEvictAccountFromAllianceAnswer();
//        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #5b ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
//        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("??? 'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();


//        System.out.println("========== scenario: #5c ==============================");
//        System.out.println("Trying to cancelation the battle (by client)...");
//        answer = battleCreationManager.cancelBattle();
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
////      ClientCancelBattleAnswer cancelBattleAnswer = (ClientCancelBattleAnswer) answer.getAnswer();
////      Assert.assertFalse("", cancelBattleAnswer.isSuccess()); //todo: реализовать метод isSuccess
        Manager.pause(800);
//        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #5e ==============================");
        System.out.println("Waiting for the cancellation of the battle (by author)...");
        ClientCancelBattleAnswer cancelBattleAnswer = new ClientCancelBattleAnswer();
        Assert.assertTrue("The battle has not been cancelled",
                waitForAsyncAnswer(cancelBattleAnswer, Packets.CANCEL_BATTLE, NEXT_WAINTING));
        Assert.assertTrue("Author is not able to cancel the battle", cancelBattleAnswer.isSuccess());
        clientConfiguration.setBattle(null);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #6 ==============================");

        Manager.pause(1500); // Ждем, пока ClientA создаст битву
        ClientTestHelper.checkAsyncMessages();

//        //todo: goBattleMarket - надо ли?
//        System.out.println("The client go to the battle market...");
//        confirm = baseManager.goBattleMarket();
//        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        System.out.println("Listening created battles...");
        confirm = battleMarketManager.listenToCreatedBattles();
        Assert.assertTrue(confirm.isConfirm());
        Assert.assertTrue("The client can not start listening to created battles", confirm.isConfirm());

        System.out.println("Waiting receiving a list of battles...");
        battleVector.clear();
        browseBattlesAnswer = new ClientBrowseBattlesAnswer();
        Assert.assertTrue("'Client C' does not receive a list of battles",
                waitForAsyncAnswer(browseBattlesAnswer, Packets.BROWSE_CREATED_BATTLES, BROWSE_CREATED_BATTLES_WAINTING));
        Assert.assertNotNull("??? The client does not receive a list of battles", browseBattlesAnswer.getAnswers());
        battles = ObjectManager.getInstance().getBattleCollection();
        Assert.assertNotNull("??? The client does not receive a list of battles", battles);
        Assert.assertTrue("Battle available to play should be one (battle count = " + battles.size() + ")", battles.size() == 1);
        battle = battles.get(0);
        clientConfiguration.setBattle(battle);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("Do not listening created battles...");
        confirm = battleMarketManager.doNotListenToCreatedBattles();
        Assert.assertTrue("The client can not stop listening to created battles", confirm.isConfirm());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6b ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        listen = battleMarketManager.listenToBattle(battle);
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
//        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6d ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
//        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("??? 'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6e ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
//        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6f ==============================");
        System.out.println("Waiting for the cancellation of the battle (by author)...");
        cancelBattleAnswer = new ClientCancelBattleAnswer();
        Assert.assertTrue("The battle has not been cancelled",
                waitForAsyncAnswer(cancelBattleAnswer, Packets.CANCEL_BATTLE, NEXT_WAINTING));
        Assert.assertTrue("Author is not able to cancel the battle", cancelBattleAnswer.isSuccess());
        clientConfiguration.setBattle(null);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #7 ==============================");

        Manager.pause(1500); // Ждем, пока ClientA создаст битву
        ClientTestHelper.checkAsyncMessages();

//        //todo: goBattleMarket - надо ли?
//        System.out.println("The client go to the battle market...");
//        confirm = baseManager.goBattleMarket();
//        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        System.out.println("Listening created battles...");
        confirm = battleMarketManager.listenToCreatedBattles();
        Assert.assertTrue(confirm.isConfirm());
        Assert.assertTrue("The client can not start listening to created battles", confirm.isConfirm());

        System.out.println("Waiting receiving a list of battles...");
        battleVector.clear();
        browseBattlesAnswer = new ClientBrowseBattlesAnswer();
        Assert.assertTrue("'Client C' does not receive a list of battles",
                waitForAsyncAnswer(browseBattlesAnswer, Packets.BROWSE_CREATED_BATTLES, BROWSE_CREATED_BATTLES_WAINTING));
        Assert.assertNotNull("??? The client does not receive a list of battles", browseBattlesAnswer.getAnswers());
        battles = ObjectManager.getInstance().getBattleCollection();
        Assert.assertNotNull("??? The client does not receive a list of battles", battles);
        Assert.assertTrue("Battle available to play should be one (battle count = " + battles.size() + ")", battles.size() == 1);
        battle = battles.get(0);
        clientConfiguration.setBattle(battle);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("Do not listening created battles...");
        confirm = battleMarketManager.doNotListenToCreatedBattles();
        Assert.assertTrue("The client can not stop listening to created battles", confirm.isConfirm());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7b ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        listen = battleMarketManager.listenToBattle(battle);
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
//        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Account accountClientA = battle.getAuthor();
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7d ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client C)...");
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
//        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("??? 'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientC.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7e ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
//        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7f ==============================");
        System.out.println("Waiting for a client's joining to the alliance (Client A)...");
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
//        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client A' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("??? 'Client A' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientA.getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        BattleAlliance alliance = joinToBattleAllianceAnswer.getBattleGroup().getAlliance();
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7g ==============================");
        System.out.println("'Client B' is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        joinToBattleAllianceAnswer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertFalse("'Client B' should not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Manager.pause(800);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7h ==============================");
        System.out.println("Waiting of completion the battle group (Client A)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
//        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client A' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client A' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client A'", accountClientA.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7i ==============================");
        System.out.println("Waiting of disbandment the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
//        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not disband the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_DISBAND, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not disband the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' disbanded the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7k ==============================");
        System.out.println("Waiting of completion the battle group (Client C)...");
        completeGroupAnswer = new ClientCompleteGroupAnswer();
//        completeGroupAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' could not complete the battle group",
                waitForAsyncAnswer(completeGroupAnswer, Packets.GROUP_COMPLETE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' could not complete the battle group", completeGroupAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client 'Client C'", accountClientC.getId() == completeGroupAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + completeGroupAnswer.getBattleGroup().getAccount().getName() +
                "' completed the battle group (battle group id = " + completeGroupAnswer.getBattleGroup().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7p ==============================");
        System.out.println("Waiting of start of the battle (by Author)...");
        ClientStartBattleAnswer startBattleAnswer = new ClientStartBattleAnswer(clientConfiguration);
        Assert.assertTrue("'Client A' does not begin the battle",
                waitForAsyncAnswer(startBattleAnswer, Packets.START_BATTLE, NEXT_WAINTING));
        Assert.assertTrue("??? The battle began with an error", startBattleAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", accountClientA.getId() == startBattleAnswer.getBattle().getAuthor().getId());
        System.out.println("Client '" + startBattleAnswer.getBattle().getAuthor().getName() +
                "' began the battle (battle id = " + startBattleAnswer.getBattle().getId() +
                ", address: " + startBattleAnswer.getHost() + ":" + startBattleAnswer.getPort() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("The test was completed successfully.");
    }

}
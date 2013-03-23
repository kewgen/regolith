package com.geargames.regolith.battleConnectionTest;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.ClientTestHelper;
import com.geargames.regolith.Packets;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

import junit.framework.Assert;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 22.01.13
 * Time: 22:45
 */
public class BattleConnectionTest {

    private static final int NEXT_WAINTING = 2000;  // 20 сек

//    private static void await(CyclicBarrier barrier) {
//        try {
//            barrier.await();
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        } catch (BrokenBarrierException e1) {
//            e1.printStackTrace();
//        }
//    }

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        try {
            return answer.retrieve(1000);
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
                Manager.pause(1000);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    public void client() throws com.geargames.regolith.RegolithException, BrokenBarrierException, InterruptedException {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("clientC", "секрет", true);

        System.out.println("Configuring the client...");

        Account selfAccount = loginAnswer.getAccount();
        System.out.println("Account id = " + selfAccount.getId());
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
//        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        ClientTestHelper.hireWarriorForClient(selfAccount);

        System.out.println("The client go to the battle market...");

        ClientDeferredAnswer answer = baseManager.goBattleManager();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        answer = battleMarketManager.browseBattles();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientBrowseBattlesAnswer browseBattlesAnswer = (ClientBrowseBattlesAnswer) answer.getAnswer();
        ClientBattleCollection battles = browseBattlesAnswer.getBattles();
        Assert.assertTrue("There is no battle to play", battles.size() > 0);
        Battle battle = battles.get(0);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #1a ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        answer = battleMarketManager.listenToBattle(battle);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientListenToBattleAnswer listen = (ClientListenToBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        BattleAlliance alliance = ClientTestHelper.getFreeAlliance(battle);
        Assert.assertNotNull("There is no empty battle group for the client", alliance);

        System.out.println("========== scenario: #1b ==============================");
        System.out.println("The client is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        answer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientJoinToBattleAllianceAnswer joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        BattleGroup battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Assert.assertNotNull("The client could not join to the alliance", battleGroup);
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == battleGroup.getAccount().getId());
        System.out.println("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (id = " + battleGroup.getAlliance().getId() + ")");
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #1c ==============================");
        System.out.println("The client is trying to get out of the battle (by himself)...");
        answer = battleCreationManager.evictAccount(alliance, selfAccount);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientEvictAccountFromAllianceAnswer evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) answer.getAnswer();
//        BattleGroup battleGroup = evictAccountFromAllianceAnswer.getBattleGroup();
        Assert.assertTrue("The client could not be evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #2a ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        answer = battleMarketManager.listenToBattle(battle);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        listen = (ClientListenToBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #2b ==============================");
        System.out.println("The client is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        answer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Assert.assertNotNull("The client could not join to the alliance", battleGroup);
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == battleGroup.getAccount().getId());
        System.out.println("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (id = " + battleGroup.getAlliance().getId() + ")");
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #2c ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (by author)...");
        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #3a ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        answer = battleMarketManager.listenToBattle(battle);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        listen = (ClientListenToBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3b ==============================");
        System.out.println("The client is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        answer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Assert.assertNotNull("The client could not join to the alliance", battleGroup);
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == battleGroup.getAccount().getId());
        System.out.println("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (id = " + battleGroup.getAlliance().getId() + ")");
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3c ==============================");
        System.out.println("The client informs about the readiness for the battle (Client C)...");
        answer = battleCreationManager.isReady(battleGroup);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientGroupReadyStateAnswer groupReadyStateAnswer = (ClientGroupReadyStateAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertNotNull("The client could not establish its readiness", groupReadyStateAnswer.getBattleGroup());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #3d ==============================");
        System.out.println("The client is trying to get out of the battle (by himself)...");
        answer = battleCreationManager.evictAccount(alliance, selfAccount);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("The client could not be evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #4a ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        answer = battleMarketManager.listenToBattle(battle);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        listen = (ClientListenToBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4b ==============================");
        System.out.println("The client is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        answer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Assert.assertNotNull("The client could not join to the alliance", battleGroup);
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == battleGroup.getAccount().getId());
        System.out.println("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (id = " + battleGroup.getAlliance().getId() + ")");
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4c ==============================");
        System.out.println("The client informs about the readiness for the battle (Client C)...");
        answer = battleCreationManager.isReady(battleGroup);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        groupReadyStateAnswer = (ClientGroupReadyStateAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertNotNull("The client could not establish its readiness", groupReadyStateAnswer.getBattleGroup());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #4d ==============================");
        System.out.println("Waiting for a client's eviction from the alliance (by author)...");
        evictAccountFromAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client C' has not evicted from the alliance",
                waitForAsyncAnswer(evictAccountFromAllianceAnswer, Packets.EVICT_ACCOUNT_FROM_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("'Client C' has not evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == evictAccountFromAllianceAnswer.getAccount().getId());
        System.out.println("Client '" + evictAccountFromAllianceAnswer.getAccount().getName() +
                "' evicted from the alliance (id = " + evictAccountFromAllianceAnswer.getAlliance().getId() + ")");
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #5a ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        answer = battleMarketManager.listenToBattle(battle);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        listen = (ClientListenToBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #5b ==============================");
        System.out.println("The client is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        answer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Assert.assertNotNull("The client could not join to the alliance", battleGroup);
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == battleGroup.getAccount().getId());
        System.out.println("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (id = " + battleGroup.getAlliance().getId() + ")");
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

//        System.out.println("========== scenario: #5d ==============================");
//        System.out.println("Trying to cancelation the battle (by client)...");
//        answer = battleCreationManager.cancelBattle();
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
////      ClientCancelBattleAnswer cancelBattleAnswer = (ClientCancelBattleAnswer) answer.getAnswer();
////      Assert.assertFalse("", cancelBattleAnswer.isSuccess()); //todo: реализовать метод isSuccess
        Manager.pause(1000);
//        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #5e ==============================");
        System.out.println("Waiting for the cancellation of the battle (by author)...");
        ClientCancelBattleAnswer cancelBattleAnswer = new ClientCancelBattleAnswer();
        Assert.assertTrue("The battle has not been cancelled",
                waitForAsyncAnswer(cancelBattleAnswer, Packets.CANCEL_BATTLE, NEXT_WAINTING));
//        Assert.assertTrue("'Client C' has not evicted from the alliance", cancelBattleAnswer.isSuccess()); //todo: реализовать метод isSuccess
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #6 ==============================");

        Manager.pause(1000); // Ждем, пока ClientA создаст битву
        ClientTestHelper.checkAsyncMessages();

        //todo: goBattleManager - надо ли?
        System.out.println("The client go to the battle market...");
        answer = baseManager.goBattleManager();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        answer = battleMarketManager.browseBattles();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        browseBattlesAnswer = (ClientBrowseBattlesAnswer) answer.getAnswer();
        battles = browseBattlesAnswer.getBattles();
        Assert.assertTrue("There is no battle to play", battles.size() > 0);
        battle = battles.get(0);
        alliance = ClientTestHelper.getFreeAlliance(battle);
        Assert.assertNotNull("There is no empty battle group for the client", alliance);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6c ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        answer = battleMarketManager.listenToBattle(battle);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        listen = (ClientListenToBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6d ==============================");
        System.out.println("The client is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        answer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Assert.assertNotNull("The client could not join to the alliance", battleGroup);
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == battleGroup.getAccount().getId());
        System.out.println("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (id = " + battleGroup.getAlliance().getId() + ")");
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6e ==============================");
        System.out.println("The client informs about the readiness for the battle (Client C)...");
        answer = battleCreationManager.isReady(battleGroup);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        groupReadyStateAnswer = (ClientGroupReadyStateAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertNotNull("The client could not establish its readiness", groupReadyStateAnswer.getBattleGroup());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #6f ==============================");
        System.out.println("Waiting for the cancellation of the battle (by author)...");
        cancelBattleAnswer = new ClientCancelBattleAnswer();
        Assert.assertTrue("The battle has not been cancelled",
                waitForAsyncAnswer(cancelBattleAnswer, Packets.CANCEL_BATTLE, NEXT_WAINTING));
//        Assert.assertTrue("'Client C' has not evicted from the alliance", cancelBattleAnswer.isSuccess()); //todo: реализовать метод isSuccess
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        // -------------------------------------------------------------------------------------------------------------

        System.out.println("========== scenario: #7 ==============================");

        Manager.pause(1000); // Ждем, пока ClientA создаст битву
        ClientTestHelper.checkAsyncMessages();

        //todo: goBattleManager - надо ли?
        System.out.println("The client go to the battle market...");
        answer = baseManager.goBattleManager();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        answer = battleMarketManager.browseBattles();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        browseBattlesAnswer = (ClientBrowseBattlesAnswer) answer.getAnswer();
        battles = browseBattlesAnswer.getBattles();
        Assert.assertTrue("There is no battle to play", battles.size() > 0);
        battle = battles.get(0);
        alliance = ClientTestHelper.getFreeAlliance(battle);
        Assert.assertNotNull("There is no empty battle group for the client", alliance);
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7c ==============================");
        System.out.println("Trying to connect to the battle for listening...");
        answer = battleMarketManager.listenToBattle(battle);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        listen = (ClientListenToBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7d ==============================");
        System.out.println("The client is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")...");
        answer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        joinToBattleAllianceAnswer = (ClientJoinToBattleAllianceAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        battleGroup = joinToBattleAllianceAnswer.getBattleGroup();
        Assert.assertNotNull("The client could not join to the alliance", battleGroup);
        Assert.assertTrue("Different ID of the client accounts", selfAccount.getId() == battleGroup.getAccount().getId());
        System.out.println("Client '" + battleGroup.getAccount().getName() +
                "' joined to the alliance (id = " + battleGroup.getAlliance().getId() + ")");
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7e ==============================");
        System.out.println("The client informs about the readiness for the battle (Client C)...");
        answer = battleCreationManager.isReady(battleGroup);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        groupReadyStateAnswer = (ClientGroupReadyStateAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertNotNull("The client could not establish its readiness", groupReadyStateAnswer.getBattleGroup());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7f ==============================");
        System.out.println("Waiting for a client's joining to the alliance (client A)...");
        joinToBattleAllianceAnswer = new ClientJoinToBattleAllianceAnswer();
        joinToBattleAllianceAnswer.setBattle(battle);
        Assert.assertTrue("'Client A' has not joined to the alliance",
                waitForAsyncAnswer(joinToBattleAllianceAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, NEXT_WAINTING));
        Assert.assertTrue("??? 'Client A' could not join to the alliance", joinToBattleAllianceAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client selfAccount", battle.getAuthor().getId() == joinToBattleAllianceAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + joinToBattleAllianceAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + joinToBattleAllianceAnswer.getBattleGroup().getAlliance().getId() + ")");
        Manager.pause(300);
        Manager.pause(300+1000); // + секунда, т.к. в это время выполняется сценарий #7g у клиента B
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7h ==============================");
        System.out.println("Waiting for a client's readiness for the battle (client A)...");
        groupReadyStateAnswer = new ClientGroupReadyStateAnswer();
        groupReadyStateAnswer.setBattle(battle);
        Assert.assertTrue("'Client A' has not establish its readiness",
                waitForAsyncAnswer(groupReadyStateAnswer, Packets.GROUP_IS_READY, NEXT_WAINTING));
        Assert.assertTrue("'Client A' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertTrue("Different ID of the client selfAccount", battle.getAuthor().getId() == groupReadyStateAnswer.getBattleGroup().getAccount().getId());
        System.out.println("Client '" + groupReadyStateAnswer.getBattleGroup().getAccount().getName() +
                "' established readiness for battle (id = " + groupReadyStateAnswer.getBattleGroup().getAlliance().getBattle().getId() + ")");
        BattleAlliance allianceClientA = groupReadyStateAnswer.getBattleGroup().getAlliance();
        Manager.pause(300);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7i ==============================");
        System.out.println("The client informs about the unreadiness for the battle (Client C)...");
        answer = battleCreationManager.isNotReady(battleGroup);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        groupReadyStateAnswer = (ClientGroupReadyStateAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertNotNull("The client could not establish its readiness", groupReadyStateAnswer.getBattleGroup());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7g ==============================");
        System.out.println("The client informs about the readiness for the battle (Client C)...");
        answer = battleCreationManager.isReady(battleGroup);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        groupReadyStateAnswer = (ClientGroupReadyStateAnswer) answer.getAnswer();
        Assert.assertTrue("'Client C' can not change their readiness for battle", groupReadyStateAnswer.isSuccess());
        Assert.assertNotNull("The client could not establish its readiness", groupReadyStateAnswer.getBattleGroup());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("========== scenario: #7n ==============================");
        System.out.println("The client is trying to evict the author of battle from the alliance...");
        answer = battleCreationManager.evictAccount(allianceClientA, battle.getAuthor());
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        evictAccountFromAllianceAnswer = (ClientEvictAccountFromAllianceAnswer) answer.getAnswer();
//        BattleGroup battleGroup = evictAccountFromAllianceAnswer.getBattleGroup();
        Assert.assertFalse("The client could not be evicted from the alliance", evictAccountFromAllianceAnswer.isSuccess());
        Manager.pause(1000);
        ClientTestHelper.checkAsyncMessages();




//        int groupSize = battle.getBattleType().getGroupSize();
//        Assert.assertTrue("The selfAccount " + selfAccount.getName() + " does not have enough warriors", groupSize <= selfAccount.getWarriors().size());
//
//        Warrior[] warriors = new Warrior[groupSize];
//        for (int i = 0; i < groupSize; i++) {
//            warriors[i] = selfAccount.getWarriors().get(i);
//        }
//
//        System.out.println("The client is trying to complete group (id = " + battleGroup.getId() + ")");
//        answer = battleCreationManager.completeGroup(battleGroup, warriors);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        ClientConfirmationAnswer clientConfirmationAnswer = (ClientConfirmationAnswer) answer.getAnswer();
//        Assert.assertTrue("The client could not complete the battle group (id = " + battleGroup.getId() + ")", clientConfirmationAnswer.isConfirm());
//
//        System.out.println("The client has completed a group");
//
//        System.out.println("The client is trying to confirm a group...");
//        answer = battleCreationManager.isReady(battleGroup);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        clientConfirmationAnswer = (ClientConfirmationAnswer) answer.getAnswer();
//        Assert.assertTrue("The client has not confirmed the group", clientConfirmationAnswer.isConfirm());
//
//        System.out.println("The client has confirmed the group.");

        System.out.println("The test was completed successfully.");
    }
}
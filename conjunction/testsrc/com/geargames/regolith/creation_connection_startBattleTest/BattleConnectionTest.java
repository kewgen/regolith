package com.geargames.regolith.creation_connection_startBattleTest;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.ClientTestHelper;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

import junit.framework.Assert;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 22.01.13
 * Time: 22:45
 */
public class BattleConnectionTest {

    private static void await(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (BrokenBarrierException e1) {
            e1.printStackTrace();
        }
    }

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        return answer.retrieve(1000);
    }

    @Test
    public void client() throws com.geargames.regolith.RegolithException, BrokenBarrierException, InterruptedException {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("clientC", "секрет", true);

        System.out.println("Configuring the client");

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
//        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        ClientTestHelper.hireWarriorForClient(account);

        System.out.println("The client go to the battle market");

        ClientDeferredAnswer answer = baseManager.goBattleManager();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue("The client could not go to the battle market", confirm.isConfirm());

        answer = battleMarketManager.battlesJoinTo();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientBrowseBattlesAnswer clientBrowseBattlesAnswer = (ClientBrowseBattlesAnswer) answer.getAnswer();
        ClientBattleCollection battles = clientBrowseBattlesAnswer.getBattles();
        Assert.assertTrue("There is no battle to play", battles.size() > 0);

        System.out.println("Trying to connect to the battle for listening");

        // scenario: #1a
        Battle battle = battles.get(0);
        answer = battleMarketManager.listenToBattle(battle, account);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientCreateBattleAnswer listen = (ClientCreateBattleAnswer) answer.getAnswer();
        battle = listen.getBattle();
        Assert.assertNotNull("The client could not listen to the battle", battle);

        int groupSize = battle.getBattleType().getGroupSize();
        Assert.assertTrue("The account " + account.getName() + " does not have enough warriors", groupSize <= account.getWarriors().size());

        Warrior[] warriors = new Warrior[groupSize];
        for (int i = 0; i < groupSize; i++) {
            warriors[i] = account.getWarriors().get(i);
        }
        BattleAlliance alliance = null;
        for (BattleAlliance battleAlliance : battle.getAlliances()) {
            for (int i = 0; i < battleAlliance.getAllies().size(); i++) {
                if (battleAlliance.getAllies().get(i).getAccount() == null) {
                    alliance = battleAlliance;
                    break;
                }
            }
            if (alliance != null) {
                break;
            }
        }
        Assert.assertNotNull("There is no empty battle group for the client", alliance);

        // scenario: #1b
        System.out.println("The client is trying join to an alliance (id = " + alliance.getId() + "; number = " + alliance.getNumber() + ")");
        answer = battleCreationManager.joinToAlliance(alliance);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientJoinBattleAnswer clientJoinBattleAnswer = (ClientJoinBattleAnswer) answer.getAnswer();
        BattleGroup battleGroup = clientJoinBattleAnswer.getBattleGroup();
        Assert.assertNotNull("The client could not join to the alliance", battleGroup);




//        System.out.println("The client is trying to complete group (id = " + battleGroup.getId() + ")");
//        answer = battleCreationManager.completeGroup(battleGroup, warriors);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        ClientConfirmationAnswer clientConfirmationAnswer = (ClientConfirmationAnswer) answer.getAnswer();
//        Assert.assertTrue("The client could not complete the battle group (id = " + battleGroup.getId() + ")", clientConfirmationAnswer.isConfirm());
//
//        System.out.println("The client has completed a group");
//
//        System.out.println("The client is trying to confirm a group");
//        answer = battleCreationManager.isReady(battleGroup);
//        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
//        clientConfirmationAnswer = (ClientConfirmationAnswer) answer.getAnswer();
//        Assert.assertTrue("The client has not confirmed the group", clientConfirmationAnswer.isConfirm());
//
//        System.out.println("The client has confirmed the group.");

        System.out.println("The test was completed successfully.");
    }
}

package com.geargames.regolith;

import com.geargames.regolith.managers.*;
import com.geargames.regolith.network.DataMessage;
import com.geargames.regolith.network.Network;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.BattleMapHelper;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * User: mikhail v. kutuzov
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
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());
        Login login = new Login();
        login.setName("автор1");
        login.setPassword("секрет");

        ClientDeferredAnswer answer = commonManager.checkForName(login.getName());
        Assert.assertTrue(waitForAnswer(answer));

        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue("a name " + login.getName() + "already exists",confirm.isConfirm());

            System.out.println();
            answer = commonManager.create(login);
            Assert.assertTrue("The client could not create the login name",waitForAnswer(answer));

            confirm = (ClientConfirmationAnswer) answer.getAnswer();
            Assert.assertTrue("The client could not createAmmunitionBag an account",!confirm.isConfirm());


        answer = commonManager.login(login);
        Assert.assertTrue(!waitForAnswer(answer));

        ClientLoginAnswer loginAnswer = (ClientLoginAnswer) answer.getAnswer();
        if (loginAnswer.getError() != null) {
            System.err.println(loginAnswer.getError());
            return;
        }

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        if (clientConfiguration.getBaseWarriors() != null) {
            Warrior[] warriors = clientConfiguration.getBaseWarriors();
            byte amount = clientConfiguration.getBaseConfiguration().getInitWarriorsAmount();
            if (warriors.length < amount) {
                System.out.println("An amount of available warriors is not enough for the client");
                return;
            }
            Warrior[] initWarriors = new Warrior[amount];
            for (int i = 0; i < amount; i++) {
                warriors[i].setName(warriors[i].getName() + "i");
                initWarriors[i] = warriors[i];
            }
            System.out.println("The client is trying to hire warriors");
            answer = baseWarriorMarketManager.hireWarrior(initWarriors);
            if (!waitForAnswer(answer)) {
                return;
            }

            ClientJoinBaseWarriorsAnswer clientJoinBaseWarriorsAnswer = (ClientJoinBaseWarriorsAnswer) answer.getAnswer();
            ClientWarriorCollection clientWarriorCollection = new ClientWarriorCollection(new Vector());
            if (clientJoinBaseWarriorsAnswer.isSuccess()) {
                for (Warrior warrior : clientJoinBaseWarriorsAnswer.getWarriors()) {
                    clientWarriorCollection.add(warrior);
                }
                account.setWarriors(clientWarriorCollection);
            } else {
                System.out.println("The client could not get a set of base warriors");
                return;
            }
        }

        System.out.println("The client go to the battle market");

        answer = baseManager.goBattleManager();
        if (!waitForAnswer(answer)) {
            return;
        }

        confirm = (ClientConfirmationAnswer) answer.getAnswer();
        if (!confirm.isConfirm()) {
            System.err.println("The client could not go to the battle market");
            return;
        }

        answer = battleMarketManager.battlesJoinTo();
        if (!waitForAnswer(answer)) {
            return;
        }
        ClientBrowseBattlesAnswer clientBrowseBattlesAnswer = (ClientBrowseBattlesAnswer) answer.getAnswer();
        ClientBattleCollection battles = clientBrowseBattlesAnswer.getBattles();

        if (battles.size() == 1) {
            Battle battle = battles.get(0);
            answer = battleMarketManager.listenToBattle(battle, account);
            if (!waitForAnswer(answer)) {
                return;
            }
            ClientCreateBattleAnswer listen = (ClientCreateBattleAnswer) answer.getAnswer();
            battle = listen.getBattle();
            if (battle != null) {
                int groupSize = battle.getBattleType().getGroupSize();
                if (groupSize <= account.getWarriors().size()) {
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
                    if (alliance == null) {
                        System.err.println("There is no empty battle group for the client");
                        return;
                    } else {
                        System.out.print("The client is trying join to an alliance " + alliance.getId() + " " + alliance.getNumber());
                        answer = battleCreationManager.joinToAlliance(alliance, account);
                        if (!waitForAnswer(answer)) {
                            return;
                        }
                        ClientJoinBattleAnswer clientJoinBattleAnswer = (ClientJoinBattleAnswer) answer.getAnswer();
                        BattleGroup battleGroup = clientJoinBattleAnswer.getBattleGroup();
                        if (battleGroup != null) {
                            System.out.print("The client is trying to complete group " + battleGroup.getId());
                            answer = battleCreationManager.completeGroup(battleGroup, warriors);
                            if (!waitForAnswer(answer)) {
                                return;
                            }
                            ClientConfirmationAnswer clientConfirmationAnswer = (ClientConfirmationAnswer) answer.getAnswer();
                            if (clientConfirmationAnswer.isConfirm()) {
                                System.out.println("The client has completed a group.");

                                System.out.println("The client is trying to confirm a group");
                                answer = battleCreationManager.isReady(battleGroup);
                                if (!waitForAnswer(answer)) {
                                    return;
                                }

                                clientConfirmationAnswer = (ClientConfirmationAnswer) answer.getAnswer();
                                if (clientConfirmationAnswer.isConfirm()) {
                                    System.out.println("The client has confirmed the group");
                                }

                                return;
                            } else {
                                System.out.println("The client could not complete the battle group " + battleGroup.getId());
                                return;
                            }
                        } else {
                            System.out.print("The client could not join to the alliance");
                            return;
                        }
                    }
                } else {
                    System.err.println("The account " + account.getName() + " does not have enough warriors");
                    return;
                }
            } else {
                System.err.println("The client could not listen to the battle");
                return;
            }
        } else {
            System.out.print("There is no battle to play");
            return;

        }

    }
}

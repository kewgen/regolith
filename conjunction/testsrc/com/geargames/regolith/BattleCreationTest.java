package com.geargames.regolith;

import com.geargames.common.util.ArrayList;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.network.DataMessage;
import com.geargames.regolith.network.Network;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.BattleMapHelper;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;

/**
 * User: mikhail v. kutuzov
 * Date: 27.06.12
 * Time: 14:51
 */
public class BattleCreationTest {
    private static SimpleService service;

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        return answer.retrieve(1000);
    }

    @BeforeClass
    public static void service() throws Exception {
        service = MainServiceManager.runMainService();
    }

    @Test
    public void client() throws com.geargames.regolith.RegolithException, BrokenBarrierException, InterruptedException {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());
        Login login = new Login();
        login.setName("автор");
        login.setPassword("секрет");

        System.out.println("Is checking for a login name");
        ClientDeferredAnswer answer = commonManager.checkForName(login.getName());

        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        if (confirm.isConfirm()) {
            System.out.println("Trying to create an account");
            answer = commonManager.create(login);
            Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

            confirm = (ClientConfirmationAnswer) answer.getAnswer();
            Assert.assertTrue("Could not create the account", confirm.isConfirm());
        }

        System.out.println("Going to log in");

        answer = commonManager.login(login);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientLoginAnswer loginAnswer = (ClientLoginAnswer) answer.getAnswer();
        Assert.assertNull(loginAnswer.getError(), loginAnswer.getError());

        System.out.println("...");

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        if (account.getWarriors().size() <= 0) {
            if (clientConfiguration.getBaseWarriors() != null) {
                Warrior[] warriors = clientConfiguration.getBaseWarriors();
                byte amount = clientConfiguration.getBaseConfiguration().getInitWarriorsAmount();
                Assert.assertTrue("An amount of available warriors is not enough", warriors.length >= amount);

                Warrior[] initWarriors = new Warrior[amount];
                for (int i = 0; i < amount; i++) {
                    warriors[i].setName(warriors[i].getName() + "i");
                    initWarriors[i] = warriors[i];
                }
                System.out.println("We are trying to hire warriors");
                answer = baseWarriorMarketManager.hireWarrior(initWarriors);
                Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

                ClientJoinBaseWarriorsAnswer clientJoinBaseWarriorsAnswer = (ClientJoinBaseWarriorsAnswer) answer.getAnswer();
                Assert.assertTrue("Could not get a set base warriors", clientJoinBaseWarriorsAnswer.isSuccess());

                ClientWarriorCollection clientWarriorCollection = new ClientWarriorCollection(new Vector());
                for (Warrior warrior : clientJoinBaseWarriorsAnswer.getWarriors()) {
                    clientWarriorCollection.add(warrior);
                }
                account.setWarriors(clientWarriorCollection);
            }
        }

        System.out.println("Lets try to move a tackle");


        Warrior warrior = account.getWarriors().get(1);
/*      StateTackle tackle = TackleTransitionHelper.moveStateTackleBag2StoreHouse(warrior, 0, account.getBase().getStoreHouse());

        confirm = new ClientConfirmationAnswer();
        ClientMoveTackleByNumber move = new ClientMoveTackleByNumber(clientConfiguration);
        move.setNumber((short) 0);
        move.setWarrior(warrior);
        move.setAmount((short) 1);
        move.setRealAmount((short) 1);
        move.setTackle(tackle);
        move.setType(Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_STORE_HOUSE);

        BatchMessageManager messanger = BatchMessageManager.getInstance();

        messanger.deferredSend(move, confirm);

        messanger.commitMessages();

        Assert.assertTrue("Waiting time answer has expired", messanger.retrieve(1000));

        ArrayList answers = messanger.getAnswer().getAnswers();

        for (int i = 0; i < answers.size(); i++) {
            confirm = (ClientConfirmationAnswer) answers.get(i);
            Assert.assertTrue(confirm.isConfirm());
        }
*/

        System.out.println("Lets go to the battle market");

        answer = baseManager.goBattleManager();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        confirm = (ClientConfirmationAnswer) answer.getAnswer();

        Assert.assertTrue(confirm.isConfirm());

        System.out.println("browsing maps");

        answer = battleMarketManager.browseBattleMaps();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientBrowseBattleMapsAnswer browseMaps = (ClientBrowseBattleMapsAnswer) answer.getAnswer();

        BattleMap[] maps = browseMaps.getBattleMaps();

        Assert.assertTrue("there are no maps", maps.length > 0);

        answer = battleMarketManager.createBattle(maps[0], 0);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientCreateBattleAnswer createBattleAnswer = (ClientCreateBattleAnswer) answer.getAnswer();
        Battle battle = createBattleAnswer.getBattle();

        Assert.assertNotNull("Could not create his own battle.", battle);

        BattleGroup group;
        BattleAlliance alliance = battle.getAlliances()[0];
        answer = battleCreationManager.joinToAlliance(alliance, account);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        group = ((ClientJoinBattleAnswer) answer.getAnswer()).getBattleGroup();
        Assert.assertNotNull("Could not joint to his own battle.", group);

        Assert.assertTrue(account.getName() + " has no enough warriors for this battle", battle.getBattleType().getGroupSize() <= account.getWarriors().size());

        Warrior[] warriors = new Warrior[battle.getBattleType().getGroupSize()];
        for (int i = 0; i < warriors.length; i++) {
            warriors[i] = account.getWarriors().get(i);
        }

        answer = battleCreationManager.completeGroup(group, warriors);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        confirm = (ClientConfirmationAnswer) answer.getAnswer();

        Assert.assertTrue("Could not put a group into his own battle.", confirm.isConfirm());

        answer = battleCreationManager.isReady(group);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue("Have not be able to be ready for his own battle.", confirm.isConfirm());

        //todo: В бесконечном цикле проверять, что все желающие подключились и выслали isReady, только после этого послать единичный startBattle
        //todo: Перед startBattle, можно попробовать выкинуть одного из игроков, после чего он снова должен подключиться и после этого уже слать startBattle

        ClientStartBattleAnswer clientStartBattleAnswer;
        while (true) {
            answer = battleCreationManager.startBattle(account);
            Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

            clientStartBattleAnswer = (ClientStartBattleAnswer) answer.getAnswer();
            if (clientStartBattleAnswer.isSuccess()) {
                break;
            } else {
                try {
                    System.out.println("The battle could not be started.");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Assert.fail("Could not start a battle and was interrupted.");
                }
            }
        }
        System.out.println("The battle has begun");

        Network network = clientConfiguration.getNetwork();
        network.disconnect();
        network.connect(clientStartBattleAnswer.getHost(), clientStartBattleAnswer.getPort());

        System.out.println("Try to login into the battle");

        ClientBattleServiceManager battleServiceManager = clientConfiguration.getBattleServiceManager();
        answer = battleServiceManager.login(battle, alliance);

        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientBattleLoginAnswer battleLoginAnswer = (ClientBattleLoginAnswer) answer.getAnswer();
        Assert.assertTrue("Could not login into the battle", battleLoginAnswer.isSuccess());

        DataMessage dataMessage;
        boolean active = false;
        while (true) {
            active = ClientHelper.isOurTurn(network, alliance, active);
            if (active) {
                break;
            } else {
                continue;
            }
        }

        System.out.println("...");

        answer = battleServiceManager.move(warriors[0], (short) 10, (short) 10);

        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));

        ClientAllyMoveAnswer clientAllyMoveAnswer = (ClientAllyMoveAnswer) answer.getAnswer();

        BattleMapHelper.clearRoutes(warriors[0], warriors[0].getX(), warriors[0].getY());
        BattleMapHelper.clearViewAround(warriors[0]);

        WarriorHelper.move(warriors[0], clientAllyMoveAnswer.getX(), clientAllyMoveAnswer.getY(), new MoveOneStepListener() {
            @Override
            public void onStep(Warrior warrior, int x, int y) {
            }
        }, clientConfiguration.getBattleConfiguration());

        BattleMap map = battle.getMap();
        for (Warrior warriorA : clientAllyMoveAnswer.getEnemies()) {
            BattleMapHelper.putIn(warriorA, map, warrior.getX(), warrior.getY());
        }
    }

    @AfterClass
    public static void stop() {
        service.stopService();
    }

}

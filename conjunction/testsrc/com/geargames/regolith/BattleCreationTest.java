package com.geargames.regolith;

import com.geargames.ConsoleDebug;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.util.ArrayList;
import com.geargames.env.ConsoleEnvironment;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.network.DataMessage;
import com.geargames.regolith.network.Network;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.answers.*;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * User: mikhail v. kutuzov
 * Date: 27.06.12
 * Time: 14:51
 */
public class BattleCreationTest {
    private static SimpleService service;

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

    @BeforeClass
    public static void service() throws Exception {
        service = MainServiceManager.runMainService();
    }

    @Test
    public void client() throws com.geargames.regolith.RegolithException, BrokenBarrierException, InterruptedException {
        SystemEnvironment.getInstance().setDebug(new ConsoleDebug());
        SystemEnvironment.getInstance().setEnvironment(ConsoleEnvironment.getInstance());

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientCommonManager commonManager = clientConfiguration.getCommonManager();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());
        Login login = new Login();
        login.setName("автор");
        login.setPassword("секрет");

        System.out.println("is checking for a login name");
        ClientDeferredAnswer answer = commonManager.checkForName(login.getName());

        if (!waitForAnswer(answer)) {
            System.err.println("Waiting time answer has expired");
            return;
        }

        ClientConfirmationAnswer confirm = (ClientConfirmationAnswer) answer.getAnswer();
        if (confirm.isConfirm()) {
            System.out.println("trying to create an account");
            answer = commonManager.create(login);
            if (!waitForAnswer(answer)) {
                System.err.println("Waiting time answer has expired");
                return;
            }

            confirm = (ClientConfirmationAnswer) answer.getAnswer();
            if (!confirm.isConfirm()) {
                System.err.println("could not create the account");
                return;
            }
        }

        System.out.println("going to log in");

        answer = commonManager.login(login);
        if (!waitForAnswer(answer)) {
            System.err.println("Waiting time answer has expired");
            return;
        }

        ClientLoginAnswer loginAnswer = (ClientLoginAnswer) answer.getAnswer();
        if (loginAnswer.getError() != null) {
            System.err.println(loginAnswer.getError());
            return;
        }

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
                if (warriors.length < amount) {
                    System.err.println("An amount of available warriors is not enough");
                    return;
                }
                Warrior[] initWarriors = new Warrior[amount];
                for (int i = 0; i < amount; i++) {
                    warriors[i].setName(warriors[i].getName() + "i");
                    initWarriors[i] = warriors[i];
                }
                System.out.println("We are trying to hire warriors");
                answer = baseWarriorMarketManager.hireWarrior(initWarriors);
                if (!waitForAnswer(answer)) {
                    System.err.println("Waiting time answer has expired");
                    return;
                }

                ClientJoinBaseWarriorsAnswer clientJoinBaseWarriorsAnswer = (ClientJoinBaseWarriorsAnswer) answer.getAnswer();
                if (clientJoinBaseWarriorsAnswer.isSuccess()) {
                    ClientWarriorCollection clientWarriorCollection = new ClientWarriorCollection(new Vector());
                    for (Warrior warrior : clientJoinBaseWarriorsAnswer.getWarriors()) {
                        clientWarriorCollection.add(warrior);
                    }
                    account.setWarriors(clientWarriorCollection);
                } else {
                    System.err.println("could not get a set base warriors");
                    return;
                }
            }
        }

        System.out.println("lets try to move a tackle");

        Warrior warrior = account.getWarriors().get(1);
        StateTackle tackle = TackleTransitionHelper.moveStateTackleBag2StoreHouse(warrior, 0, account.getBase().getStoreHouse());

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

        Assert.assertTrue(messanger.retrieve(1000));

        ArrayList answers = messanger.getAnswer().getAnswers();

        for (int i = 0; i < answers.size(); i++) {
            confirm = (ClientConfirmationAnswer) answers.get(i);
            Assert.assertTrue(confirm.isConfirm());
        }

        System.out.println("lets go to the battle market");

        answer = baseManager.goBattleManager();
        Assert.assertTrue(waitForAnswer(answer));

        confirm = (ClientConfirmationAnswer) answer.getAnswer();

        Assert.assertTrue(confirm.isConfirm());

        System.out.println("browsing maps");

        answer = battleMarketManager.browseBattleMaps();
        Assert.assertTrue(waitForAnswer(answer));

        ClientBrowseBattleMapsAnswer browseMaps = (ClientBrowseBattleMapsAnswer) answer.getAnswer();

        BattleMap[] maps = browseMaps.getBattleMaps();

        Assert.assertTrue("there are no maps", maps.length > 0);


        answer = battleMarketManager.createBattle(maps[0], 0);
        Assert.assertTrue(waitForAnswer(answer));
        ClientCreateBattleAnswer createBattle = (ClientCreateBattleAnswer) answer.getAnswer();
        Battle battle = createBattle.getBattle();

        Assert.assertNotNull("could not create his own battle.", battle);

        BattleGroup group;
        BattleAlliance alliance = battle.getAlliances()[0];
        answer = battleCreationManager.joinToAlliance(alliance, account);
        Assert.assertTrue(waitForAnswer(answer));

        group = ((ClientJoinBattleAnswer) answer.getAnswer()).getBattleGroup();
        Assert.assertNotNull("could not joint to his own battle.", group);

        Assert.assertTrue(account.getName() + " has no enough warriors for this battle", battle.getBattleType().getGroupSize() <= account.getWarriors().size());

        Warrior[] warriors = new Warrior[battle.getBattleType().getGroupSize()];
        for (int i = 0; i < warriors.length; i++) {
            warriors[i] = account.getWarriors().get(i);
        }

        answer = battleCreationManager.completeGroup(group, warriors);
        Assert.assertTrue(waitForAnswer(answer));

        confirm = (ClientConfirmationAnswer) answer.getAnswer();

        Assert.assertTrue("could not put a group into his own battle.", confirm.isConfirm());

        answer = battleCreationManager.isReady(group);
        Assert.assertTrue(waitForAnswer(answer));

        confirm = (ClientConfirmationAnswer) answer.getAnswer();
        Assert.assertTrue("have not be able to be ready for his own battle.", confirm.isConfirm());
        ClientStartBattleAnswer clientStartBattleAnswer;
        while (true) {
            answer = battleCreationManager.startBattle(account);
            if (!waitForAnswer(answer)) {
                System.err.println("Waiting time answer has expired");
                return;
            }

            clientStartBattleAnswer = (ClientStartBattleAnswer) answer.getAnswer();
            if (clientStartBattleAnswer.isSuccess()) {
                break;
            }

            try {
                System.out.println("the battle could not be started.");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("could not start a battle and was interrupted.");
                return;
            }
        }
        System.out.println("The battle has begun");

        Network network = clientConfiguration.getNetwork();
        network.disconnect();
        network.connect(clientStartBattleAnswer.getHost(), clientStartBattleAnswer.getPort());

        System.out.println("Try to login into the battle");

        ClientBattleServiceManager battleServiceManager = clientConfiguration.getBattleServiceManager();
        answer = battleServiceManager.login(battle, alliance);

        if (!waitForAnswer(answer)) {
            System.err.println("Waiting time answer has expired");
            return;
        }

        ClientBattleLoginAnswer battleLoginAnswer = (ClientBattleLoginAnswer) answer.getAnswer();
        if (!battleLoginAnswer.isSuccess()) {
            System.err.println("Could not login into the battle");
        }

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

        if (!waitForAnswer(answer)) {
            System.err.println("Waiting time answer has expired");
            return;
        }

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

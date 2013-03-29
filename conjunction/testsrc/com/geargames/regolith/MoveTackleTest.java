package com.geargames.regolith;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.common.util.ArrayList;
import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.BatchMessageManager;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientJoinBaseWarriorsAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.serializers.requests.ClientMoveTackleByNumber;
import com.geargames.regolith.service.MainServiceManager;
import com.geargames.regolith.service.SimpleService;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.TackleTransitionHelper;
import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 27.06.12, 19.03.13
 */
public class MoveTackleTest {
    private static SimpleService service;

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        try {
            return answer.retrieve(1000);
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
    public void client() throws RegolithException, BrokenBarrierException, InterruptedException {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("автор", "секрет", true);

        System.out.println("...");

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseWarriorMarketManager baseWarriorMarketManager = clientConfiguration.getBaseWarriorMarketManager();

        ClientDeferredAnswer answer;
        ClientConfirmationAnswer confirm;

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
        try {
            ArrayList answers = messanger.commitMessages().getAnswers();

            for (int i = 0; i < answers.size(); i++) {
                confirm = (ClientConfirmationAnswer) answers.get(i);
                Assert.assertTrue(confirm.isConfirm());
            }

            System.out.println("Tackle was moved. The test was completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void stop() {
        service.stopService();
    }

}
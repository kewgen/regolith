package com.geargames.regolith.creation_connection_startBattleTest;

import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.ClientTestHelper;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.answers.ClientBrowseBattlesAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientCreateBattleAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;
import junit.framework.Assert;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;

/**
 * User: abarakov
 * Date: 19.03.13
 */
public class BattleListenerTest {

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        return answer.retrieve(1000);
    }

    @Test
    public void client() throws com.geargames.regolith.RegolithException, BrokenBarrierException, InterruptedException {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("clientB", "секрет", true);

        System.out.println("Configuring the client");

        Account account = loginAnswer.getAccount();
        clientConfiguration.setBaseConfiguration(loginAnswer.getBaseConfiguration());
        clientConfiguration.setBaseWarriors(loginAnswer.getWarriors());

        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();
//        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
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

        Battle battle = battles.get(0);
        answer = battleMarketManager.listenToBattle(battle, account);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientCreateBattleAnswer listen = (ClientCreateBattleAnswer) answer.getAnswer();
        battle = listen.getBattle();
        Assert.assertNotNull("The client could not listen to the battle", battle);


//        ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer()




        System.out.println("The test was completed successfully.");
    }

}
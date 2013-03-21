package com.geargames.regolith.creation_connection_startBattleTest;

import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.ClientTestHelper;
import com.geargames.regolith.Packets;
import com.geargames.regolith.application.Manager;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.answers.*;
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

    private static final int FIRST_WAINTING = 1000; // 100 сек
    private static final int NEXT_WAINTING  = 200;  // 20 сек

    private static boolean waitForAnswer(ClientDeferredAnswer answer) {
        return answer.retrieve(1000);
    }

    // Ожидаем асинхронного сообщения до 5 сек и возвращаем true, если сообщение получено.
    private static boolean waitForAsyncAnswer(ClientDeSerializedMessage answer, short msgType, int attemptCount) {
        int i = 0;
        while (! ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(answer, msgType)) {
            if (i++ >= attemptCount) {
                return false;
            }
            Manager.pause(100);
        }
        return true;
    }

    @Test
    public void client() throws com.geargames.regolith.RegolithException, BrokenBarrierException, InterruptedException {
        ConsoleMainHelper.appInitialize();

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        clientConfiguration.getNetwork().connect(clientConfiguration.getServer(), clientConfiguration.getPort());
        ClientTestHelper.checkAsyncMessages();

        ClientLoginAnswer loginAnswer = ClientTestHelper.clientLogon("clientB", "секрет", true);
        ClientTestHelper.checkAsyncMessages();

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
        ClientTestHelper.checkAsyncMessages();

        answer = battleMarketManager.battlesJoinTo();
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientBrowseBattlesAnswer clientBrowseBattlesAnswer = (ClientBrowseBattlesAnswer) answer.getAnswer();
        ClientBattleCollection battles = clientBrowseBattlesAnswer.getBattles();
        Assert.assertTrue("There is no battle to play", battles.size() > 0);
        ClientTestHelper.checkAsyncMessages();

        System.out.println("Trying to connect to the battle for listening");

        // scenario: #0b
        Battle battle = battles.get(0);
        answer = battleMarketManager.listenToBattle(battle, account);
        Assert.assertTrue("Waiting time answer has expired", waitForAnswer(answer));
        ClientCreateBattleAnswer listen = (ClientCreateBattleAnswer) answer.getAnswer();
        Assert.assertNotNull("The client could not listen to the battle", listen.getBattle());
        Assert.assertTrue("Different references to the battles", battle == listen.getBattle());
        ClientTestHelper.checkAsyncMessages();

        System.out.println("Listening battle...");

        // scenario: #1b
        ClientJoinBattleAnswer clientJoinBattleAnswer = new ClientJoinBattleAnswer(battle);
//        ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(
//                clientJoinBattleAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE);
        Assert.assertTrue("'Client C' has not joined to the alliance",
                waitForAsyncAnswer(clientJoinBattleAnswer, Packets.JOIN_TO_BATTLE_ALLIANCE, FIRST_WAINTING));
        System.out.println(
                "Client '" + clientJoinBattleAnswer.getBattleGroup().getAccount().getName() +
                "' joined to the alliance (id = " + clientJoinBattleAnswer.getBattleGroup().getAlliance().getId() + ")");
        ClientTestHelper.checkAsyncMessages();

//        System.out.println("Listening battle completed");

        //todo: GROUP_IS_READY     -> ACCOUNT_IS_READY
        //todo: GROUP_IS_NOT_READY -> ACCOUNT_IS_NOT_READY

//        DO_NOT_LISTEN_TO_BATTLE

//        ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(Packets.GROUP_COMPLETE);
//        ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(Packets.CANCEL_BATTLE);
//        ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(Packets.START_BATTLE);
//        ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(Packets.EVICT_ACCOUNT_FROM_ALLIANCE);
//        ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(Packets.GROUP_IS_READY);
//        ClientConfigurationFactory.getConfiguration().getNetwork().getAsynchronousAnswer(Packets.GROUP_IS_NOT_READY);






        System.out.println("The test was completed successfully.");
    }

}
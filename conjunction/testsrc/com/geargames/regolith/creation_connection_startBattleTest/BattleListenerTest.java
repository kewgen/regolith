package com.geargames.regolith.creation_connection_startBattleTest;

import com.geargames.platform.ConsoleMainHelper;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.ClientLoginHelper;
import com.geargames.regolith.managers.ClientDeferredAnswer;
import com.geargames.regolith.serializers.answers.ClientLoginAnswer;
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

        ClientLoginAnswer loginAnswer = ClientLoginHelper.clientLogon("clientC", "секрет", true);





        System.out.println("The test was completed successfully.");
    }

}
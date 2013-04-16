package com.geargames.regolith;

import com.geargames.common.util.Lock;
import com.geargames.platform.network.ConsoleNetwork;
import com.geargames.platform.util.JavaLock;
import com.geargames.regolith.managers.*;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.network.RegolithMessageDispatcher;

/**
 * @author Mikhail_Kutuzov
 *         created: 25.05.12  13:08
 */
public class ClientConfigurationFactory {
    private static ClientConfiguration configuration;
    private static Lock lock = new JavaLock();

    public static ClientConfiguration getConfiguration() {
        if (configuration == null) {
            lock.lock();
            if (configuration == null) {
                configuration = produce();
            }
            lock.release();
        }
        return configuration;
    }

    public static ClientConfiguration produce() {
        ClientConfiguration configuration = new ClientConfiguration();

        configuration.setMaxErrorsAmount(3);
        int SIZE = 4096;
        configuration.setIncomingMessage(new byte[SIZE]);
        configuration.setOutgoingMessageSize(256);

        configuration.setMessageBuffer(new MicroByteBuffer(new byte[SIZE]));
        configuration.setNetwork(new ConsoleNetwork(new MicroByteBuffer(new byte[SIZE])));
        configuration.setPort(1238);
        configuration.setServer("localhost");

        configuration.setMessageDispatcher(new RegolithMessageDispatcher(configuration.getNetwork(), Packets.BATTLE_SERVICE_NEW_CLIENT_LOGIN));
        configuration.setBattleCreationManager(new ClientBattleCreationManager(configuration));
        configuration.setCommonManager(new ClientCommonManager(configuration));
        configuration.setBattleMarketManager(new ClientBattleMarketManager(configuration));
        configuration.setBaseManager(new ClientBaseManager(configuration));
        configuration.setBaseWarriorMarketManager(new ClientBaseWarriorMarketManager(configuration));



        short[] stepLengths = new short[8];
        stepLengths[0] = (short)Port.getConvertedValue(63);
        stepLengths[1] = (short)Port.getConvertedValue(63);
        stepLengths[2] = (short)Port.getConvertedValue(63);
        stepLengths[3] = (short)Port.getConvertedValue(63);
        stepLengths[4] = (short)Port.getConvertedValue(63);
        stepLengths[5] = (short)Port.getConvertedValue(63);
        stepLengths[6] = (short)Port.getConvertedValue(63);
        stepLengths[7] = (short)Port.getConvertedValue(63);
        configuration.setStepLengths(stepLengths);

        return configuration;
    }

}

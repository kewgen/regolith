package com.geargames.regolith;

import com.geargames.common.util.Lock;
import com.geargames.regolith.application.MELock;
import com.geargames.regolith.managers.*;
import com.geargames.regolith.network.ConsoleNetwork;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.MicroByteBuffer;

/**
 * @author Mikhail_Kutuzov
 *         created: 25.05.12  13:08
 */
public class ClientConfigurationFactory {
    private static ClientConfiguration configuration;
    private static Lock lock = new MELock();

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

        configuration.setNetwork(new ConsoleNetwork(configuration));
        configuration.setPort(1237);
        configuration.setServer("localhost");
        configuration.setMaxErrorsAmount(3);
        int SIZE = 4096;
        configuration.setIncomingMessage(new byte[SIZE]);
        configuration.setOutgoingMessageSize(256);
        Lock lock = new MELock();
        MessageLock messageLock = new MessageLock();
        messageLock.setLock(lock);
        lock.lock();
        configuration.setMessageLock(messageLock);

        configuration.setMessageBuffer(new MicroByteBuffer(new byte[SIZE]));
        configuration.setAnswersBuffer(new MicroByteBuffer());

        configuration.setBattleCreationManager(new ClientBattleCreationManager(configuration));
        configuration.setCommonManager(new ClientCommonManager(configuration));
        configuration.setBattleMarketManager(new ClientBattleMarketManager(configuration));
        configuration.setBaseManager(new ClientBaseManager(configuration));
        configuration.setBaseWarriorMarketManager(new ClientBaseWarriorMarketManager(configuration));

        return configuration;
    }

}

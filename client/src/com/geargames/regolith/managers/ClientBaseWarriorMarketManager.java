package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.answers.ClientJoinBaseWarriorsAnswer;
import com.geargames.regolith.serializers.requests.ClientJoinBaseWarriorsRequest;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Date: 02.07.12
 * Time: 9:45
 */
public class ClientBaseWarriorMarketManager {
    private ClientConfiguration configuration;

    public ClientBaseWarriorMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Послать сообщение о найме текущим аккаунтом списка бойцов.
     */
    public ClientDeferredAnswer hireWarrior(Warrior[] warriors) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.JOIN_BASE_WARRIORS_TO_ACCOUNT);
        ClientDeSerializedMessage message = new ClientJoinBaseWarriorsAnswer(warriors, configuration.getBaseConfiguration());
        messageLock.setMessage(message);
        configuration.getNetwork().sendMessage(new ClientJoinBaseWarriorsRequest(configuration, warriors));

        return new ClientDeferredAnswer(message);
    }
}

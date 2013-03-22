package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
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
    private ClientJoinBaseWarriorsAnswer message;

    public ClientBaseWarriorMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        message = new ClientJoinBaseWarriorsAnswer();
        message.setBaseConfiguration(configuration.getBaseConfiguration());
    }

    /**
     * Послать сообщение о найме текущим аккаунтом списка бойцов.
     */
    public ClientDeferredAnswer hireWarrior(Warrior[] warriors) {
        message.setWarriors(warriors);
        return configuration.getNetwork().sendSynchronousMessage(new ClientJoinBaseWarriorsRequest(configuration, warriors), message);
    }
}

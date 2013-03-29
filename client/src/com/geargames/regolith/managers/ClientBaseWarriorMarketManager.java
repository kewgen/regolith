package com.geargames.regolith.managers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.answers.ClientJoinBaseWarriorsAnswer;
import com.geargames.regolith.serializers.requests.ClientJoinBaseWarriorsRequest;
import com.geargames.regolith.units.battle.Warrior;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 02.07.12
 */
public class ClientBaseWarriorMarketManager {
    private ClientConfiguration configuration;
    private ClientJoinBaseWarriorsAnswer joinBaseWarriorsAnswer;

    public ClientBaseWarriorMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        joinBaseWarriorsAnswer = new ClientJoinBaseWarriorsAnswer(configuration);
    }

    /**
     * Послать сообщение о найме текущим аккаунтом списка бойцов.
     */
    public ClientJoinBaseWarriorsAnswer hireWarrior(Warrior[] warriors) throws Exception {
        joinBaseWarriorsAnswer.setWarriors(warriors);
        configuration.getNetwork().sendSynchronousMessage(
                new ClientJoinBaseWarriorsRequest(configuration, warriors), joinBaseWarriorsAnswer, 100);
        return joinBaseWarriorsAnswer;
    }

}

package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.network.RegolithDeferredAnswer;
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
    private ClientDeferredAnswer answer;

    public ClientBaseWarriorMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        joinBaseWarriorsAnswer = new ClientJoinBaseWarriorsAnswer(configuration);
        answer = new RegolithDeferredAnswer();
    }

    /**
     * Послать сообщение о найме текущим аккаунтом списка бойцов.
     */
    public ClientDeferredAnswer hireWarrior(Warrior[] warriors) {
        answer.setDeSerializedMessage(joinBaseWarriorsAnswer);
        joinBaseWarriorsAnswer.setWarriors(warriors);
        configuration.getNetwork().sendSynchronousMessage(
                new ClientJoinBaseWarriorsRequest(configuration, warriors), answer);
        return answer;
    }

}

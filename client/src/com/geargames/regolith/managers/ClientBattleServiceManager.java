package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.network.RegolithDeferredAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.serializers.requests.ClientBattleServiceLoginRequest;
import com.geargames.regolith.serializers.requests.ClientMoveRequest;
import com.geargames.regolith.serializers.requests.ClientShootRequest;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.Warrior;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 15.08.12
 */
public class ClientBattleServiceManager {
    private ClientConfiguration configuration;
    private ClientListenToBattleAnswer clientListenToBattleAnswer;

    private ClientDeferredAnswer answer;

    public ClientBattleServiceManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        clientListenToBattleAnswer = new ClientListenToBattleAnswer();
        answer = new RegolithDeferredAnswer();
    }

    public ClientDeferredAnswer login(Battle battle, BattleAlliance alliance) {
        answer.setDeSerializedMessage(clientListenToBattleAnswer);
        configuration.getNetwork().sendSynchronousMessage(
                new ClientBattleServiceLoginRequest(configuration, battle, alliance, configuration.getAccount()),
                answer);
        return answer;
    }

    //todo все вызовы ниже сделать асинхронными
    public ClientDeferredAnswer move(Warrior warrior, short x, short y) {
        //todo ожидается ответ неправильного типа
        answer.setDeSerializedMessage(clientListenToBattleAnswer);
        configuration.getNetwork().sendSynchronousMessage(
                new ClientMoveRequest(configuration, warrior, x, y),
                answer);
        return answer;
    }

    public ClientDeferredAnswer shoot(Warrior hunter, Warrior victim) {
        //todo ожидается ответ неправильного типа
        answer.setDeSerializedMessage(clientListenToBattleAnswer);
        configuration.getNetwork().sendSynchronousMessage(
                new ClientShootRequest(configuration, hunter, victim),
                answer);
        return answer;
    }

}

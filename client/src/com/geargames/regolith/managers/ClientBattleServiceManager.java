package com.geargames.regolith.managers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.ClientConfiguration;
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

    public ClientBattleServiceManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        clientListenToBattleAnswer = new ClientListenToBattleAnswer();
    }

    public ClientDeSerializedMessage login(Battle battle, BattleAlliance alliance) throws Exception{
        configuration.getNetwork().sendSynchronousMessage(
                new ClientBattleServiceLoginRequest(configuration, battle, alliance, configuration.getAccount()),
                clientListenToBattleAnswer,100);
        return clientListenToBattleAnswer;
    }

    //todo все вызовы ниже сделать асинхронными
    public ClientDeSerializedMessage move(Warrior warrior, short x, short y) throws Exception {
        //todo ожидается ответ неправильного типа
        configuration.getNetwork().sendSynchronousMessage(
                new ClientMoveRequest(configuration, warrior, x, y),
                clientListenToBattleAnswer, 100);
        return clientListenToBattleAnswer;
    }

    public ClientDeSerializedMessage shoot(Warrior hunter, Warrior victim) throws Exception {
        //todo ожидается ответ неправильного типа
        configuration.getNetwork().sendSynchronousMessage(
                new ClientShootRequest(configuration, hunter, victim),
                clientListenToBattleAnswer,100);
        return clientListenToBattleAnswer;
    }

}

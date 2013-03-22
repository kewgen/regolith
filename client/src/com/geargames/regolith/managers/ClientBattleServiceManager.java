package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.answers.ClientCreateBattleAnswer;
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
    private ClientCreateBattleAnswer clientCreateBattleAnswer;

    public ClientBattleServiceManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        clientCreateBattleAnswer = new ClientCreateBattleAnswer();
    }

    public ClientDeferredAnswer login(Battle battle, BattleAlliance alliance) {
        return configuration.getNetwork().sendSynchronousMessage(
                new ClientBattleServiceLoginRequest(configuration, battle, alliance, configuration.getAccount()),
                clientCreateBattleAnswer);
    }

    //todo все вызовы ниже сделать асинхронными

    public ClientDeferredAnswer move(Warrior warrior, short x, short y) {
        //todo ожидается ответ неправильного типа
        return configuration.getNetwork().sendSynchronousMessage(
                new ClientMoveRequest(configuration, warrior, x, y),
                clientCreateBattleAnswer);
    }

    public ClientDeferredAnswer shoot(Warrior hunter, Warrior victim) {
        //todo ожидается ответ неправильного типа
        return configuration.getNetwork().sendSynchronousMessage(
                new ClientShootRequest(configuration, hunter, victim),
                clientCreateBattleAnswer);
    }

}

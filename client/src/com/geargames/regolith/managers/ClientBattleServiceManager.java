package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.serializers.answers.ClientMoveWarriorAnswer;
import com.geargames.regolith.serializers.requests.ClientBattleServiceLoginRequest;
import com.geargames.regolith.serializers.requests.ClientCheckSumRequest;
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
    private ClientMoveWarriorAnswer clientMoveMyWarriorAnswer;
    private ClientCheckSumRequest checkSumRequest;

    public ClientBattleServiceManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        clientListenToBattleAnswer = new ClientListenToBattleAnswer();
        checkSumRequest = new ClientCheckSumRequest();
        checkSumRequest.setConfiguration(configuration);
        clientMoveMyWarriorAnswer = new ClientMoveWarriorAnswer();
    }

    public ClientListenToBattleAnswer login(Battle battle, BattleAlliance alliance) throws Exception {
        configuration.getNetwork().sendSynchronousMessage(
                new ClientBattleServiceLoginRequest(configuration, battle, alliance, configuration.getAccount()),
                clientListenToBattleAnswer, 30000);
        return clientListenToBattleAnswer;
    }

    /**
     * Пытаемся двинуть своего бойца warrior в точку (x;y).
     *
     * @param warrior
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    public ClientMoveWarriorAnswer move(Warrior warrior, short x, short y) throws Exception {
        clientMoveMyWarriorAnswer.setBattle(warrior.getBattleGroup().getAlliance().getBattle());
        configuration.getNetwork().sendSynchronousMessage(
                new ClientMoveRequest(configuration, warrior, x, y),
                clientMoveMyWarriorAnswer, 100000);
        return clientMoveMyWarriorAnswer;
    }


    /**
     * Посылаем проверочную сумму в конце каждого хода.
     */
    public void checkSum() {
        checkSumRequest.setAccount(configuration.getAccount());
        configuration.getNetwork().sendMessage(checkSumRequest);
    }

    public ClientListenToBattleAnswer shoot(Warrior hunter, Warrior victim) throws Exception {
        //todo ожидается ответ неправильного типа
        configuration.getNetwork().sendSynchronousMessage(
                new ClientShootRequest(configuration, hunter, victim),
                clientListenToBattleAnswer, 100);
        return clientListenToBattleAnswer;
    }

}

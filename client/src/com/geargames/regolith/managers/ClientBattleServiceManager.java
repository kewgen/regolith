package com.geargames.regolith.managers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.serializers.answers.ClientMoveMyWarriorAnswer;
import com.geargames.regolith.serializers.requests.ClientBattleServiceLoginRequest;
import com.geargames.regolith.serializers.requests.ClientCheckSumRequest;
import com.geargames.regolith.serializers.requests.ClientMoveRequest;
import com.geargames.regolith.serializers.requests.ClientShootRequest;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.HumanElement;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 15.08.12
 */
public class ClientBattleServiceManager {
    private ClientConfiguration configuration;
    private ClientListenToBattleAnswer clientListenToBattleAnswer;
    private ClientMoveMyWarriorAnswer clientMoveMyWarriorAnswer;
    private ClientCheckSumRequest checkSumRequest;

    public ClientBattleServiceManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        clientListenToBattleAnswer = new ClientListenToBattleAnswer();
        checkSumRequest = new ClientCheckSumRequest();
        checkSumRequest.setConfiguration(configuration);
        clientMoveMyWarriorAnswer = new ClientMoveMyWarriorAnswer();
        clientMoveMyWarriorAnswer.setEnemies(PRegolithPanelManager.getInstance().getBattleScreen().getEnemyUnits());
    }

    public ClientDeSerializedMessage login(Battle battle, BattleAlliance alliance) throws Exception {
        configuration.getNetwork().sendSynchronousMessage(
                new ClientBattleServiceLoginRequest(configuration, battle, alliance, configuration.getAccount()),
                clientListenToBattleAnswer, 30000);
        return clientListenToBattleAnswer;
    }

    /**
     * Пытаемся двинуть своего бойца unit в точку (x;y).
     *
     * @param unit
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    public ClientDeSerializedMessage move(HumanElement unit, short x, short y) throws Exception {
        clientMoveMyWarriorAnswer.setHumanElement(unit);
        configuration.getNetwork().sendSynchronousMessage(
                new ClientMoveRequest(configuration, unit, x, y),
                clientMoveMyWarriorAnswer, 100);
        return clientListenToBattleAnswer;
    }


    /**
     * Посылаем проверочную сумму в конце каждого хода.
     */
    public void checkSum() {
        checkSumRequest.setAccount(configuration.getAccount());
        configuration.getNetwork().sendMessage(checkSumRequest);
    }

    public ClientDeSerializedMessage shoot(Warrior hunter, Warrior victim) throws Exception {
        //todo ожидается ответ неправильного типа
        configuration.getNetwork().sendSynchronousMessage(
                new ClientShootRequest(configuration, hunter, victim),
                clientListenToBattleAnswer, 100);
        return clientListenToBattleAnswer;
    }

}

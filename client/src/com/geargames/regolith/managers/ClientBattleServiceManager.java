package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.answers.ClientCreateBattleAnswer;
import com.geargames.regolith.serializers.requests.ClientBattleServiceLoginRequest;
import com.geargames.regolith.serializers.requests.ClientMoveRequest;
import com.geargames.regolith.serializers.requests.ClientShootRequest;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Date: 15.08.12
 * Time: 13:32
 */
public class ClientBattleServiceManager {
    private ClientConfiguration configuration;

    public ClientBattleServiceManager(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public ClientDeferredAnswer login(Battle battle, BattleAlliance alliance) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.BATTLE_SERVICE_LOGIN);
        ClientCreateBattleAnswer clientCreateBattleAnswer = new ClientCreateBattleAnswer();
        messageLock.setMessage(clientCreateBattleAnswer);
        configuration.getNetwork().sendMessage(new ClientBattleServiceLoginRequest(configuration, battle, alliance, configuration.getAccount()));
        return new ClientDeferredAnswer(clientCreateBattleAnswer);
    }

    public ClientDeferredAnswer move(Warrior warrior, short x, short y) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.MOVE_ALLY);
        ClientCreateBattleAnswer clientCreateBattleAnswer = new ClientCreateBattleAnswer();
        messageLock.setMessage(clientCreateBattleAnswer);
        configuration.getNetwork().sendMessage(new ClientMoveRequest(configuration, warrior, x, y));
        return new ClientDeferredAnswer(clientCreateBattleAnswer);
    }

    public ClientDeferredAnswer shoot(Warrior hunter, Warrior victim) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.SHOOT);
        ClientCreateBattleAnswer clientCreateBattleAnswer = new ClientCreateBattleAnswer();
        messageLock.setMessage(clientCreateBattleAnswer);
        configuration.getNetwork().sendMessage(new ClientShootRequest(configuration, hunter, victim));
        return new ClientDeferredAnswer(clientCreateBattleAnswer);
    }


}

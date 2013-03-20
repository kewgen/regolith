package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.answers.ClientBrowseBattleMapsAnswer;
import com.geargames.regolith.serializers.answers.ClientBrowseBattlesAnswer;
import com.geargames.regolith.serializers.answers.ClientCreateBattleAnswer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.map.BattleMap;

/**
 * @author Mikhail_Kutuzov
 *         created: 22.05.12  18:22
 */
public class ClientBattleMarketManager {
    private ClientConfiguration configuration;

    public ClientBattleMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public ClientDeferredAnswer createBattle(BattleMap battleMap, int index) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.CREATE_BATTLE);
        ClientCreateBattleAnswer clientCreateBattleAnswer = new ClientCreateBattleAnswer();
        messageLock.setMessage(clientCreateBattleAnswer);
        configuration.getNetwork().sendMessage(new CreateBattleRequest(configuration, battleMap, (byte) index));

        return new ClientDeferredAnswer(clientCreateBattleAnswer);
    }

    public ClientDeferredAnswer listenToBattle(Battle battle, Account participant) {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.LISTEN_TO_BATTLE);
        ClientCreateBattleAnswer clientCreateBattleAnswer = new ClientCreateBattleAnswer();
        messageLock.setMessage(clientCreateBattleAnswer);
        configuration.getNetwork().sendMessage(new ListenToBattleRequest(configuration, battle));

        return new ClientDeferredAnswer(clientCreateBattleAnswer);
    }

    public ClientDeferredAnswer battlesJoinTo() {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.BROWSE_CREATED_BATTLES);
        ClientBrowseBattlesAnswer clientBrowseBattlesAnswer = new ClientBrowseBattlesAnswer(configuration.getBaseConfiguration());
        messageLock.setMessage(clientBrowseBattlesAnswer);
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.BROWSE_CREATED_BATTLES));

        return new ClientDeferredAnswer(clientBrowseBattlesAnswer);
    }

    public ClientDeferredAnswer browseBattleMaps() {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.BROWSE_BATTLE_MAPS);
        ClientBrowseBattleMapsAnswer clientBrowseBattleMapsAnswer = new ClientBrowseBattleMapsAnswer(configuration.getBaseConfiguration());
        messageLock.setMessage(clientBrowseBattleMapsAnswer);
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.BROWSE_BATTLE_MAPS));

        return new ClientDeferredAnswer(clientBrowseBattleMapsAnswer);
    }

    public void goToBase() {
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.GO_TO_BASE));
    }
}

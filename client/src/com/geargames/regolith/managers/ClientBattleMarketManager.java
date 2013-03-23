package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientBrowseBattleMapsAnswer;
import com.geargames.regolith.serializers.answers.ClientBrowseBattlesAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Data: 22.05.12
 */
public class ClientBattleMarketManager {
    private ClientConfiguration configuration;
    private ClientListenToBattleAnswer listenToBattleAnswer;
    private ClientBrowseBattlesAnswer browseBattlesAnswer;
    private ClientBrowseBattleMapsAnswer browseBattleMapsAnswer;

    public ClientBattleMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        listenToBattleAnswer = new ClientListenToBattleAnswer();
        browseBattlesAnswer = new ClientBrowseBattlesAnswer(configuration);
        browseBattleMapsAnswer = new ClientBrowseBattleMapsAnswer(configuration);
    }

    public ClientDeferredAnswer createBattle(BattleMap battleMap, int index) {
        listenToBattleAnswer.setBattle(null);
        return configuration.getNetwork().sendSynchronousMessage(
                new CreateBattleRequest(configuration, battleMap, (byte) index), listenToBattleAnswer);
    }

    public ClientDeferredAnswer listenToBattle(Battle battle) {
        listenToBattleAnswer.setBattle(battle);
        return configuration.getNetwork().sendSynchronousMessage(
                new ListenToBattleRequest(configuration, battle), listenToBattleAnswer);
    }

    public ClientDeferredAnswer browseBattles() {
        return configuration.getNetwork().sendSynchronousMessage(
                new SimpleRequest(configuration, Packets.BROWSE_CREATED_BATTLES), browseBattlesAnswer);
    }

    public ClientDeferredAnswer browseBattleMaps() {
        return configuration.getNetwork().sendSynchronousMessage(
                new SimpleRequest(configuration, Packets.BROWSE_BATTLE_MAPS), browseBattleMapsAnswer);
    }

    public void goToBase() {
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.GO_TO_BASE));
    }

}

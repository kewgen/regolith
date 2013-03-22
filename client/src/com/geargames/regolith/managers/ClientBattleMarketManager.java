package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientBrowseBattleMapsAnswer;
import com.geargames.regolith.serializers.answers.ClientBrowseBattlesAnswer;
import com.geargames.regolith.serializers.answers.ClientCreateBattleAnswer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: Mikhail_Kutuzov, abarakov
 * Data: 22.05.12
 */
public class ClientBattleMarketManager {
    private ClientConfiguration configuration;
    private ClientCreateBattleAnswer createBattleAnswer;
    private ClientBrowseBattlesAnswer browseBattlesAnswer;
    ClientBrowseBattleMapsAnswer browseBattleMapsAnswer;
    public ClientBattleMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        createBattleAnswer = new ClientCreateBattleAnswer();
        browseBattlesAnswer = new ClientBrowseBattlesAnswer(configuration.getBaseConfiguration());
        browseBattleMapsAnswer = new ClientBrowseBattleMapsAnswer(configuration.getBaseConfiguration());
    }

    public ClientDeferredAnswer createBattle(BattleMap battleMap, int index) {
        createBattleAnswer.setBattle(null);
        return configuration.getNetwork().sendSynchronousMessage(new CreateBattleRequest(configuration, battleMap, (byte) index), createBattleAnswer);
    }

    public ClientDeferredAnswer listenToBattle(Battle battle) {
        createBattleAnswer.setBattle(battle);
        return configuration.getNetwork().sendSynchronousMessage(new ListenToBattleRequest(configuration, battle), createBattleAnswer);
    }

    public ClientDeferredAnswer battlesJoinTo() {
        return configuration.getNetwork().sendSynchronousMessage(new SimpleRequest(configuration, Packets.BROWSE_CREATED_BATTLES), browseBattlesAnswer);
    }

    public ClientDeferredAnswer browseBattleMaps() {
        return configuration.getNetwork().sendSynchronousMessage(new SimpleRequest(configuration, Packets.BROWSE_BATTLE_MAPS), browseBattleMapsAnswer);
    }

    public void goToBase() {
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.GO_TO_BASE));
    }
}

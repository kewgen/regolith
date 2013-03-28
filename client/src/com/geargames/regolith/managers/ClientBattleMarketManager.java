package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.RegolithDeferredAnswer;
import com.geargames.regolith.serializers.answers.ClientBrowseBattleMapsAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Data: 22.05.12
 */
public class ClientBattleMarketManager {
    private ClientConfiguration configuration;
    private ClientListenToBattleAnswer listenToBattleAnswer;
    private ClientBrowseBattleMapsAnswer browseBattleMapsAnswer;
    private ClientConfirmationAnswer confirmationAnswer;

    private ClientDeferredAnswer answer;

    public ClientBattleMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        listenToBattleAnswer = new ClientListenToBattleAnswer();
        confirmationAnswer = new ClientConfirmationAnswer();
        browseBattleMapsAnswer = new ClientBrowseBattleMapsAnswer(configuration);
        answer = new RegolithDeferredAnswer();
    }

    public ClientDeferredAnswer createBattle(BattleMap battleMap, BattleType battleType) {
        answer.setDeSerializedMessage(listenToBattleAnswer);
        listenToBattleAnswer.setBattle(null);
        configuration.getNetwork().sendSynchronousMessage(
                new CreateBattleRequest(configuration, battleMap, battleType), answer);
        return answer;
    }

    public ClientDeferredAnswer listenToBattle(Battle battle) {
        answer.setDeSerializedMessage(listenToBattleAnswer);
        listenToBattleAnswer.setBattle(battle);
        configuration.getNetwork().sendSynchronousMessage(
                new ListenToBattleRequest(configuration, battle), answer);
        return answer;
    }

    public ClientDeferredAnswer listenToCreatedBattles() {
        answer.setDeSerializedMessage(confirmationAnswer);
        configuration.getNetwork().sendSynchronousMessage(
                new SimpleRequest(configuration, Packets.LISTEN_TO_BROWSED_CREATED_BATTLES), answer);
        return answer;
    }

    public ClientDeferredAnswer doNotListenToCreatedBattles(){
        answer.setDeSerializedMessage(confirmationAnswer);
        configuration.getNetwork().sendSynchronousMessage(
                new SimpleRequest(configuration, Packets.DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES), answer);
        return answer;
    }

    public ClientDeferredAnswer browseBattleMaps() {
        answer.setDeSerializedMessage(confirmationAnswer);
        configuration.getNetwork().sendSynchronousMessage(
                new SimpleRequest(configuration, Packets.BROWSE_BATTLE_MAPS), answer);
        return answer;
    }

    public void goToBase() {
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.GO_TO_BASE));
    }

}

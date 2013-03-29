package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientBrowseBattleMapsAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
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
    private ClientBrowseBattleMapsAnswer browseBattleMapsAnswer;
    private ClientConfirmationAnswer confirmationAnswer;

    public ClientBattleMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;
        listenToBattleAnswer = new ClientListenToBattleAnswer();
        confirmationAnswer = new ClientConfirmationAnswer();
        browseBattleMapsAnswer = new ClientBrowseBattleMapsAnswer(configuration);
    }

    // todo: использовать battleTypeId вместо battleTypeIndex?
    public ClientListenToBattleAnswer createBattle(BattleMap battleMap, int battleTypeIndex) throws Exception {
        listenToBattleAnswer.setBattle(null);
        configuration.getNetwork().sendSynchronousMessage(
                new CreateBattleRequest(configuration, battleMap, (byte) battleTypeIndex), listenToBattleAnswer, 100);
        return listenToBattleAnswer;
    }

    public ClientListenToBattleAnswer listenToBattle(Battle battle) throws Exception {
        listenToBattleAnswer.setBattle(battle);
        configuration.getNetwork().sendSynchronousMessage(
                new ListenToBattleRequest(configuration, battle), listenToBattleAnswer, 100);
        return listenToBattleAnswer;
    }

    public ClientConfirmationAnswer listenToCreatedBattles() throws Exception {
        configuration.getNetwork().sendSynchronousMessage(
                new SimpleRequest(configuration, Packets.LISTEN_TO_BROWSED_CREATED_BATTLES), confirmationAnswer, 100);
        return confirmationAnswer;
    }

    public ClientConfirmationAnswer doNotListenToCreatedBattles() throws Exception {
        configuration.getNetwork().sendSynchronousMessage(
                new SimpleRequest(configuration, Packets.DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES), confirmationAnswer, 100);
        return confirmationAnswer;
    }

    public ClientBrowseBattleMapsAnswer browseBattleMaps() throws Exception {
            configuration.getNetwork().sendSynchronousMessage(
                new SimpleRequest(configuration, Packets.BROWSE_BATTLE_MAPS), browseBattleMapsAnswer, 100);
        return browseBattleMapsAnswer;
    }

    public void goToBase() {
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.GO_TO_BASE));
    }

}

package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientBattleMapAnswer;
import com.geargames.regolith.serializers.answers.ClientBattleMapListAnswer;
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
    private ClientBrowseRandomBattleMapRequest browseRandomBattleMapRequest;
    private ClientBrowseBattleMapsRequest browseBattleMapsRequest;

    private ClientConfiguration configuration;
    private ClientListenToBattleAnswer listenToBattleAnswer;
    private ClientConfirmationAnswer confirmationAnswer;
    private ClientBattleMapAnswer battleMapAnswer;
    private ClientBattleMapListAnswer battleMapListAnswer;

    public ClientBattleMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;

        browseRandomBattleMapRequest = new ClientBrowseRandomBattleMapRequest(configuration);
        browseBattleMapsRequest = new ClientBrowseBattleMapsRequest(configuration);

        listenToBattleAnswer = new ClientListenToBattleAnswer();
        confirmationAnswer = new ClientConfirmationAnswer();
        battleMapAnswer = new ClientBattleMapAnswer();
        battleMapListAnswer = new ClientBattleMapListAnswer(configuration);
    }

    public ClientListenToBattleAnswer createBattle(BattleMap battleMap, BattleType battleType) throws Exception {
        listenToBattleAnswer.setBattle(null);
        configuration.getNetwork().sendSynchronousMessage(
                new CreateBattleRequest(configuration, battleMap, battleType), listenToBattleAnswer, 100);
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

    public ClientBattleMapAnswer browseRandomBattleMap(BattleType battleType) throws Exception {
        browseRandomBattleMapRequest.setBattleType(battleType);
        configuration.getNetwork().sendSynchronousMessage(browseRandomBattleMapRequest, battleMapAnswer, 100);
        return battleMapAnswer;
    }

    public ClientBattleMapListAnswer browseBattleMaps(BattleType battleType) throws Exception {
        browseBattleMapsRequest.setBattleType(battleType);
        configuration.getNetwork().sendSynchronousMessage(browseBattleMapsRequest, battleMapListAnswer, 100);
        return battleMapListAnswer;
    }

    public void goToBase() {
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.GO_TO_BASE));
    }

}

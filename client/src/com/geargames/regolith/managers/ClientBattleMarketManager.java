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

    private ClientDeferredAnswer answer;
    private ClientConfiguration configuration;
    private ClientListenToBattleAnswer listenToBattleAnswer;
    private ClientConfirmationAnswer confirmationAnswer;
    private ClientBattleMapAnswer battleMapAnswer;
    private ClientBattleMapListAnswer battleMapListAnswer;

    public ClientBattleMarketManager(ClientConfiguration configuration) {
        this.configuration = configuration;

        browseRandomBattleMapRequest = new ClientBrowseRandomBattleMapRequest(configuration);
        browseBattleMapsRequest = new ClientBrowseBattleMapsRequest(configuration);

        answer = new RegolithDeferredAnswer();
        listenToBattleAnswer = new ClientListenToBattleAnswer();
        confirmationAnswer = new ClientConfirmationAnswer();
        battleMapAnswer = new ClientBattleMapAnswer();
        battleMapListAnswer = new ClientBattleMapListAnswer(configuration);
        browseBattleMapsAnswer = new ClientBrowseBattleMapsAnswer(configuration);
    }

    // todo: использовать battleTypeId вместо battleTypeIndex?
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

//    public ClientDeferredAnswer browseBattleMaps() {
//        answer.setDeSerializedMessage(confirmationAnswer);
//        configuration.getNetwork().sendSynchronousMessage(
//                new SimpleRequest(configuration, Packets.BROWSE_BATTLE_MAPS), answer);
//        return answer;
//    }

    public ClientDeferredAnswer browseRandomBattleMap(BattleType battleType) {
        browseRandomBattleMapRequest.setBattleType(battleType);
        answer.setDeSerializedMessage(battleMapAnswer);
        configuration.getNetwork().sendSynchronousMessage(browseRandomBattleMapRequest, answer);
        return answer;
    }

    public ClientBrowseBattleMapsAnswer browseBattleMaps(BattleType battleType) throws Exception {
        browseBattleMapsRequest.setBattleType(battleType);
        answer.setDeSerializedMessage(battleMapListAnswer);
        configuration.getNetwork().sendSynchronousMessage(browseBattleMapsRequest, answer);
        return answer;
    }

    public void goToBase() {
        configuration.getNetwork().sendMessage(new SimpleRequest(configuration, Packets.GO_TO_BASE));
    }

}

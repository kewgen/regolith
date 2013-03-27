package com.geargames.regolith.managers;

import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.RegolithDeferredAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ChangeBaseLocation;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Базовый интерфейс для перехода на разные части базы.
 */
public class ClientBaseManager {
    private ClientConfiguration configuration;
    private ClientConfirmationAnswer confirmation;
    private ClientDeferredAnswer answer;

    public ClientBaseManager(ClientConfiguration clientConfiguration){
        configuration = clientConfiguration;
        confirmation = new ClientConfirmationAnswer();
        answer = new RegolithDeferredAnswer();
    }

    public ClientDeferredAnswer goWarriorMarket() {
        answer.setDeSerializedMessage(confirmation);
        configuration.getNetwork().sendSynchronousMessage(
                new ChangeBaseLocation(configuration, Packets.GO_TO_WARRIOR_MARKET), answer);
        return answer;
    }

    public ClientDeferredAnswer goTackleMarket() {
        answer.setDeSerializedMessage(confirmation);
        configuration.getNetwork().sendSynchronousMessage(
                new ChangeBaseLocation(configuration, Packets.GO_TO_TACKLE_MARKET), answer);
        return answer;
    }

    public ClientDeferredAnswer goBattleManager() {
        answer.setDeSerializedMessage(confirmation);
        configuration.getNetwork().sendSynchronousMessage(
                new ChangeBaseLocation(configuration, Packets.GO_TO_BATTLE_MARKET), answer);
        return answer;
    }

}

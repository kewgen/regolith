package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ChangeBaseLocation;

/**
 * Базовый интерфейс для перехода на разные части базы.
 */
public class ClientBaseManager {
    private ClientConfiguration configuration;
    private ClientConfirmationAnswer confirmation;

    public ClientBaseManager(ClientConfiguration clientConfiguration){
        configuration = clientConfiguration;
        confirmation = new ClientConfirmationAnswer();
    }

    public ClientDeferredAnswer goWarriorMarket() {
        return  configuration.getNetwork().sendSynchronousMessage(new ChangeBaseLocation(configuration, Packets.GO_TO_WARRIOR_MARKET), confirmation);
    }

    public ClientDeferredAnswer goTackleMarket() {
        return configuration.getNetwork().sendSynchronousMessage(new ChangeBaseLocation(configuration, Packets.GO_TO_TACKLE_MARKET), confirmation);
    }

    public ClientDeferredAnswer goBattleManager() {
        return configuration.getNetwork().sendSynchronousMessage(new ChangeBaseLocation(configuration, Packets.GO_TO_BATTLE_MARKET), confirmation);
    }
}

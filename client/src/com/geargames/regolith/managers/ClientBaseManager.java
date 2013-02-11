package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ChangeBaseLocation;

/**
 * Базовый интерфейс для перехода на разные части базы.
 */
public class ClientBaseManager {
    private ClientConfiguration configuration;

    public ClientBaseManager(ClientConfiguration clientConfiguration){
        configuration = clientConfiguration;
    }

    public ClientDeferredAnswer goWarriorMarket() {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.GO_TO_WARRIOR_MARKET);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new ChangeBaseLocation(configuration, Packets.GO_TO_WARRIOR_MARKET));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }

    public ClientDeferredAnswer goTackleMarket() {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.GO_TO_TACKLE_MARKET);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new ChangeBaseLocation(configuration, Packets.GO_TO_TACKLE_MARKET));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }

    public ClientDeferredAnswer goBattleManager() {
        MessageLock messageLock = configuration.getMessageLock();
        messageLock.setMessageType(Packets.GO_TO_BATTLE_MARKET);
        ClientConfirmationAnswer clientConfirmationAnswer = new ClientConfirmationAnswer();
        messageLock.setMessage(clientConfirmationAnswer);
        configuration.getNetwork().sendMessage(new ChangeBaseLocation(configuration, Packets.GO_TO_BATTLE_MARKET));

        return new ClientDeferredAnswer(clientConfirmationAnswer);
    }
}

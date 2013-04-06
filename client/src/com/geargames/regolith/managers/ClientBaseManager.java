package com.geargames.regolith.managers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.requests.ChangeBaseLocation;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Базовый интерфейс для перехода на разные части базы.
 */
public class ClientBaseManager {
    private ClientConfiguration configuration;
    private ClientConfirmationAnswer confirmation;

    public ClientBaseManager(ClientConfiguration clientConfiguration){
        configuration = clientConfiguration;
        confirmation = new ClientConfirmationAnswer();
    }

    public ClientConfirmationAnswer goWarriorMarket() throws Exception {
        configuration.getNetwork().sendSynchronousMessage(
                new ChangeBaseLocation(configuration, Packets.GO_TO_WARRIOR_MARKET), confirmation, 100);
        return confirmation;
    }

    public ClientConfirmationAnswer goTackleMarket() throws Exception {
       configuration.getNetwork().sendSynchronousMessage(
                new ChangeBaseLocation(configuration, Packets.GO_TO_TACKLE_MARKET), confirmation, 100);
       return confirmation;
    }

    public ClientConfirmationAnswer goBattleMarket() throws Exception{
        configuration.getNetwork().sendSynchronousMessage(
                new ChangeBaseLocation(configuration, Packets.GO_TO_BATTLE_MARKET), confirmation, 100);
        return confirmation;
    }

}

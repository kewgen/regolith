package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.states.ClientAtBattleMarket;
import com.geargames.regolith.units.battle.Battle;

/**
 * Users: m.v.kutuzov, abarakov
 * Date: 06.04.13
 */
public class ServerDoNotListenToCreatedBattleRequest extends MainOneToClientRequest {

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        Battle battle = configuration.getServerContext().getBattleManagerContext().getBattlesById().get(SimpleDeserializer.deserializeInt(from));
        if (battle != null) {
            if (configuration.getBattleCreationManager().doNotListenToBattle(battle, client.getAccount())) {
                client.setState(new ClientAtBattleMarket());
                return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.DO_NOT_LISTEN_TO_CREATED_BATTLE);
            }
        }
        return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.DO_NOT_LISTEN_TO_CREATED_BATTLE);
    }

}

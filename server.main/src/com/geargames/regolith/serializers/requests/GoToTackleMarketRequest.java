package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.states.ClientAtTackleMarket;

/**
 *
 */
public class GoToTackleMarketRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        client.setState(new ClientAtTackleMarket());
        return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.GO_TO_TACKLE_MARKET);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.states.ClientAtWarriorMarket;

/**
 * Перейти на рынок закупки бойцов.
 */
public class GoToWarriorMarketRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        client.setState(new ClientAtWarriorMarket());
        return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.GO_TO_WARRIOR_MARKET);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;

/**
 * User: mikhail v. kutuzov
 * Date: 25.08.12
 * Time: 22:28
 */
public class ServerCheckSumRequest extends BattleServiceOneToClientRequest {
    @Override
    protected SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        if (SimpleDeserializer.deserializeInt(from) == client.getAccount().getSecurity().getObserve()) {
            return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.CONTROL_SUM);
        } else {
            return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.CONTROL_SUM);
        }
    }
}

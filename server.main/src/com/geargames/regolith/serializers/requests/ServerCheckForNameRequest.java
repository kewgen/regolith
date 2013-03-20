package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public class ServerCheckForNameRequest extends MainOneToClientRequest {

    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer to, Client client) {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        String name = SimpleDeserializer.deserializeString(from);
        if (serverConfiguration.getCommonManager().checkForName(name)) {
            return ServerConfirmationAnswer.answerSuccess(to, Packets.CHECK_FOR_NAME);
        } else {
            return ServerConfirmationAnswer.answerFailure(to, Packets.CHECK_FOR_NAME);
        }
    }

}

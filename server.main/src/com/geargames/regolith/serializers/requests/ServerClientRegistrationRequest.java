package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.Login;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public class ServerClientRegistrationRequest extends MainOneToClientRequest {
    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer to, Client client) {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        Login login = new Login();
        login.setName(SimpleDeserializer.deserializeString(from));
        login.setPassword(SimpleDeserializer.deserializeString(from));
        if (serverConfiguration.getCommonManager().create(login)) {
            return ServerConfirmationAnswer.answerSuccess(to, Packets.CLIENT_REGISTRATION);
        } else {
            return ServerConfirmationAnswer.answerFailure(to, Packets.CLIENT_REGISTRATION);
        }
    }
}

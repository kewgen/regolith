package com.geargames.regolith.service.states;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.requests.ServerCheckForNameRequest;
import com.geargames.regolith.serializers.requests.ServerClientRegistrationRequest;
import com.geargames.regolith.serializers.requests.ServerLoginRequest;
import com.geargames.regolith.service.*;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 11.06.12
 */
public class ClientNotLoggedIn extends MainState {

    @Override
    public void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        List<MessageToClient> messages;
        switch (type) {
            case Packets.CHECK_FOR_NAME:
                messages = new ServerCheckForNameRequest().request(from, getWriteBuffer(), client);
                break;
            case Packets.CLIENT_REGISTRATION:
                messages = new ServerClientRegistrationRequest().request(from, getWriteBuffer(), client);
                break;
            case Packets.LOGIN:
                messages = new ServerLoginRequest().request(from, getWriteBuffer(), client);
                break;
            default:
                throw new RegolithException("Invalid message type (" + type + ")");
        }
        MainServerConfigurationFactory.getConfiguration().getWriter().addMessageToClient(messages.get(0));
    }
}

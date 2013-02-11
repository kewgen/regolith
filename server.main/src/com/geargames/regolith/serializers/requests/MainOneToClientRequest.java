package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainMessageToClient;
import com.geargames.regolith.service.MessageToClient;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public abstract class MainOneToClientRequest extends ServerRequest {
    public abstract SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws  RegolithException {
        ArrayList<SocketChannel> channels = new ArrayList<SocketChannel>(1);
        channels.add(client.getChannel());
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        messages.add(new MainMessageToClient(channels, clientRequest(from, to, client).serialize()));
        return messages;
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.service.BattleMessageToClient;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public abstract class BattleServiceOneToClientRequest extends ServerRequest {
    protected abstract SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws  RegolithException {
        ArrayList<SocketChannel> channels = new ArrayList<SocketChannel>(1);
        channels.add(client.getChannel());
        List<MessageToClient> messages = new ArrayList<MessageToClient>(1);
        messages.add(new BattleMessageToClient(channels, clientRequest(from, to, client).serialize()));
        return messages;
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: olga
 * Date: 12.07.12
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class ServerRequest {
    public abstract List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException;
}

package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 12.07.12
 */
public abstract class ServerRequest {

    public abstract List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException;

}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 21.07.12
 */
public class ServerExitBattleRequest extends ServerRequest {


    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {


        return null;
    }
}

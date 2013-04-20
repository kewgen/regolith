package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;

import java.util.LinkedList;
import java.util.List;

/**
 * User: mvkutuzov
 * Date: 20.04.13
 * Time: 17:48
 */
public class ServerBattleServiceLogoutRequest extends ServerRequest{
    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {
        List<MessageToClient> list  = new LinkedList<MessageToClient>();
        return list;
    }
}

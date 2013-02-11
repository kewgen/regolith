package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.units.battle.ServerBattle;

import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 01.10.12
 * Time: 15:56
 */
public class ServerExitRequest extends ServerRequest {
    private ServerBattle serverBattle;

    @Override
    public List<MessageToClient> request(MicroByteBuffer from, MicroByteBuffer to, Client client) throws RegolithException {

        return null;
    }
}

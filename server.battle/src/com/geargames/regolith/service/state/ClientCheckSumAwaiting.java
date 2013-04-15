package com.geargames.regolith.service.state;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.requests.ServerCheckSumRequest;
import com.geargames.regolith.serializers.requests.ServerRequest;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;

/**
 * User: mikhail v. kutuzov
 * Date: 24.08.12
 * Time: 13:56
 */
public class ClientCheckSumAwaiting extends BattleState {
    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        ServerRequest request;
        switch (type) {
            case Packets.CONTROL_SUM:
                request = new ServerCheckSumRequest();
                break;
            default:
                throw new RegolithException();
        }
        BattleServiceConfigurationFactory.getConfiguration().getWriter().addMessageToClient(request.request(from, getWriteBuffer(), client).get(0));
        client.setState(new ClientActivationAwaiting());
    }
}

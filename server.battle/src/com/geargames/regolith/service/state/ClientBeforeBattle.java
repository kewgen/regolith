package com.geargames.regolith.service.state;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.requests.BattleServiceLoginRequest;
import com.geargames.regolith.serializers.requests.ServerRequest;
import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.ClientWriter;
import com.geargames.regolith.service.MessageToClient;
import com.geargames.regolith.service.clientstates.ClientState;

/**
 * User: mikhail v. kutuzov
 * Date: 14.08.12
 * Time: 16:28
 */
public class ClientBeforeBattle extends BattleState {
    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        ServerRequest request;
        switch (type){
            case Packets.BATTLE_SERVICE_LOGIN:
                request = new BattleServiceLoginRequest();
                break;
            default:
                throw new RegolithException();
        }
        ClientWriter writer = BattleServiceConfigurationFactory.getConfiguration().getWriter();
        for(MessageToClient messageToClient : request.request(from, getWriteBuffer(), client)){
            writer.addMessageToClient(messageToClient);
        }
    }
}

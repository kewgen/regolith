package com.geargames.regolith.service.states;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.requests.ServerJoinBaseWarriorsRequest;
import com.geargames.regolith.service.*;

import java.util.List;


/**
 * User: mkutuzov
 * Date: 06.07.12
 */
public class ClientAtBaseWarriorsMarket extends MainState {
    private ClientWriter writer;

    public ClientAtBaseWarriorsMarket(){
        writer = MainServerConfigurationFactory.getConfiguration().getWriter();
    }

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        List<MessageToClient> messages;
        switch (type) {
            case Packets.JOIN_BASE_WARRIORS_TO_ACCOUNT:
                messages = new ServerJoinBaseWarriorsRequest().request(from, getWriteBuffer(), client);
                break;
            default:
                throw new RegolithException();
        }
        writer.addMessageToClient(messages.get(0));
    }
}

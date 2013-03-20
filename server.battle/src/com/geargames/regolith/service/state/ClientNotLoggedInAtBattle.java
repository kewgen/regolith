package com.geargames.regolith.service.state;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.requests.BattleServiceLoginRequest;
import com.geargames.regolith.serializers.requests.ServerRequest;
import com.geargames.regolith.service.*;

import java.util.List;

public class ClientNotLoggedInAtBattle extends BattleState {
    private ClientWriter writer;

    public ClientNotLoggedInAtBattle() {
        BattleServiceConfiguration battleServiceConfiguration = BattleServiceConfigurationFactory.getConfiguration();
        this.writer = battleServiceConfiguration.getWriter();
    }

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        ServerRequest request;
        switch (type) {
            case Packets.LOGIN:
                request = new BattleServiceLoginRequest();
                break;
            default:
                throw new RuntimeException();
        }
        List<MessageToClient> messages = request.request(from, getWriteBuffer(), client);
        for (MessageToClient message : messages) {
            writer.addMessageToClient(message);
        }
    }
}

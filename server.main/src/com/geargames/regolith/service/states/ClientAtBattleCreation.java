package com.geargames.regolith.service.states;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.service.*;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 21.06.12
 */
public class ClientAtBattleCreation extends MainState {

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        ServerRequest request;
        switch (type) {
            case Packets.GROUP_COMPLETE:
                request = new ServerGroupAddWarriorsRequest();
                break;
            case Packets.GROUP_INCOMPLETE:
                request = new ServerGroupRemoveWarriorsRequest();
                break;
            case Packets.CANCEL_BATTLE:
                request = new ServerCancelBattleRequest(client.getAccount());
                break;
            case Packets.START_BATTLE:
                request = new ServerStartBattleRequest();
                break;
            case Packets.JOIN_TO_BATTLE_ALLIANCE:
                request = new ServerJoinToBattleAllianceRequest();
                break;
            case Packets.EVICT_ACCOUNT_FROM_ALLIANCE:
                request = new ServerEvictAccountFromAllianceRequest();
                break;
            case Packets.FRAME_MESSAGE:
                request = new ServerGetFrameRequest();
                break;
            default:
                throw new RegolithException("Invalid message type (" + type + ")");
        }
        List<MessageToClient> messages = request.request(from, getWriteBuffer(), client);
        ClientWriter writer = MainServerConfigurationFactory.getConfiguration().getWriter();
        for (MessageToClient message : messages) {
            writer.addMessageToClient(message);
        }
    }
}

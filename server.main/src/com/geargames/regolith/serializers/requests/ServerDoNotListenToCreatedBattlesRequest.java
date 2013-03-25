package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.BrowseBattlesSchedulerService;
import com.geargames.regolith.service.Client;

/**
 * User: mkutuzov
 * Date: 25.03.13
 */
public class ServerDoNotListenToCreatedBattlesRequest extends MainOneToClientRequest {
    private BrowseBattlesSchedulerService schedulerService;

    public ServerDoNotListenToCreatedBattlesRequest(BrowseBattlesSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        schedulerService.removeListener(client);
        return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.BrowseBattlesSchedulerService;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;

/**
 * User: mkutuzov
 * Date: 25.03.13
 */
public class ServerListenToBrowsedCreatedBattlesRequest extends MainOneToClientRequest {
    private BrowseBattlesSchedulerService schedulerService;

    public ServerListenToBrowsedCreatedBattlesRequest() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        this.schedulerService = configuration.getBrowseBattlesSchedulerService();
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        schedulerService.addListener(client);
        return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.LISTEN_TO_BROWSED_CREATED_BATTLES);
    }

}

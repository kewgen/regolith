package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.serializers.answers.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.MainServerConfigurationFactory;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public class ServerGoToBaseRequest extends MainOneToClientRequest {

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        MainServerConfigurationFactory.getConfiguration().getBrowseBattlesSchedulerService().removeListener(client);
        return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.GO_TO_BASE);
    }

}

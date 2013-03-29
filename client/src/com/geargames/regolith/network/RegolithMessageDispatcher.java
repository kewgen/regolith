package com.geargames.regolith.network;

import com.geargames.common.network.DataMessage;
import com.geargames.common.network.MessageDispatcher;
import com.geargames.common.network.Network;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientBrowseBattlesAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;

/**
 * User: m.v.kutuzov
 * Date: 28.03.13
 */
public class RegolithMessageDispatcher extends MessageDispatcher {

    public RegolithMessageDispatcher(Network network, int size) {
        super(network, size);
    }

    @Override
    protected ClientDeSerializedMessage getMessage(DataMessage dataMessage) {
        MicroByteBuffer buffer = new MicroByteBuffer();
        buffer.initiate(dataMessage.getData(), dataMessage.getLength());

        switch (dataMessage.getMessageType()) {
            case Packets.BROWSE_CREATED_BATTLES:
                ClientBrowseBattlesAnswer browsBattles = new ClientBrowseBattlesAnswer();
                browsBattles.setBuffer(buffer);
                return  browsBattles;
            case Packets.LISTEN_TO_BROWSED_CREATED_BATTLES:
                ClientConfirmationAnswer listernToBattles = new ClientConfirmationAnswer();
                listernToBattles.setBuffer(buffer);
                return listernToBattles;
            case Packets.DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES:
                ClientConfirmationAnswer doNotListernToBattles = new ClientConfirmationAnswer();
                doNotListernToBattles.setBuffer(buffer);
                return doNotListernToBattles;
        }
        return null;
    }
}

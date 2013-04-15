package com.geargames.regolith.network;

import com.geargames.common.logging.Debug;
import com.geargames.common.network.DataMessage;
import com.geargames.common.network.MessageDispatcher;
import com.geargames.common.network.Network;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.*;

/**
 * Users: m.v.kutuzov, abarakov
 * Date: 28.03.13
 */
public class RegolithMessageDispatcher extends MessageDispatcher {

    public RegolithMessageDispatcher(Network network, int size) {
        super(network, size);
    }

    @Override
    protected ClientDeSerializedMessage getMessage(DataMessage dataMessage) {
        ClientDeSerializedMessage message;
        switch (dataMessage.getMessageType()) {
            case Packets.BROWSE_CREATED_BATTLES:
                message = new ClientBrowseBattlesAnswer();
                break;
            case Packets.LISTEN_TO_BROWSED_CREATED_BATTLES:
            case Packets.DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES:
                message = new ClientConfirmationAnswer();
                break;
            case Packets.JOIN_TO_BATTLE_ALLIANCE:
                message = new ClientJoinToBattleAllianceAnswer();
                break;
            case Packets.EVICT_ACCOUNT_FROM_ALLIANCE:
                message = new ClientEvictAccountFromAllianceAnswer();
                break;
            case Packets.GROUP_COMPLETE:
            case Packets.GROUP_DISBAND:
                message = new ClientCompleteGroupAnswer();
                break;
            case Packets.CANCEL_BATTLE:
                message = new ClientCancelBattleAnswer();
                break;
            case Packets.START_BATTLE:
                message = new ClientStartBattleAnswer();
                break;
            default:
                Debug.critical("RegolithMessageDispatcher: Unknown message type (" + dataMessage.getMessageType() + ")");
                return null;
        }
        MicroByteBuffer buffer = new MicroByteBuffer();
        buffer.initiate(dataMessage.getData(), dataMessage.getLength());
        message.setBuffer(buffer);
        return message;
    }

}

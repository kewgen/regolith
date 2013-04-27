package com.geargames.regolith.network;

import com.geargames.common.logging.Debug;
import com.geargames.common.network.DataMessage;
import com.geargames.common.network.MessageDispatcher;
import com.geargames.common.network.Network;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.serializers.answers.*;

/**
 * Users: m.v.kutuzov, abarakov
 * Date: 28.03.13
 */
public class RegolithMessageDispatcher extends MessageDispatcher {
    private ClientConfiguration configuration;

    public RegolithMessageDispatcher(Network network, int size, ClientConfiguration configuration) {
        super(network, size);
        this.configuration = configuration;
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
            case Packets.BATTLE_SERVICE_LOGIN:
                message = new ClientBattleLoginAnswer();
                ((ClientBattleLoginAnswer)message).setBattle(configuration.getBattle());
                break;
            case Packets.BATTLE_SERVICE_NEW_CLIENT_LOGIN:
                 message = new ClientNewBattleLogin();
                ((ClientNewBattleLogin)message).setBattle(configuration.getBattle());
                break;
            case Packets.CHANGE_ACTIVE_ALLIANCE:
                message = new ClientChangeActiveAllianceAnswer();
                ((ClientChangeActiveAllianceAnswer)message).setBattle(configuration.getBattle());
                break;
            case Packets.MOVE_WARRIOR:
                ClientMoveWarriorAnswer move = new ClientMoveWarriorAnswer();
                move.setBattle(configuration.getBattle());
                move.setEnemies(PRegolithPanelManager.getInstance().getBattleScreen().getEnemies());
                message = move;
                break;
            case Packets.INITIALLY_OBSERVED_ENEMIES:
                ClientInitiallyObservedEnemies init = new ClientInitiallyObservedEnemies();
                init.setBattle(configuration.getBattle());
                message = init;
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

    @Override
    protected void unhandledMessageHandler(DataMessage message) {
        Debug.debug(message.getMessageType() + " was not handled");
    }
}

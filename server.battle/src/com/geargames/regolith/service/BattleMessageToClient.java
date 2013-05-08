package com.geargames.regolith.service;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.managers.CommonBattleManager;
import com.geargames.regolith.serializers.BattleServiceRequestUtils;
import com.geargames.regolith.serializers.answers.ServerCloseBattleMessage;
import com.geargames.regolith.units.battle.ServerBattle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 30.08.12
 * Time: 10:39
 */
public class BattleMessageToClient extends MessageToClient {
    private static Logger logger = LoggerFactory.getLogger(BattleMessageToClient.class);
    private boolean isError;

    public static ThreadLocal<ByteBuffer> BYTE_BUFFER = new ThreadLocal<ByteBuffer>() {
        @Override
        protected ByteBuffer initialValue() {
            return ByteBuffer.allocateDirect(BattleServiceConfigurationFactory.getConfiguration().getMessageBufferSize());
        }
    };


    public BattleMessageToClient(Collection<SocketChannel> recipients, byte[] message) {
        super(recipients, message);
        isError = false;
    }

    @Override
    protected ByteBuffer getWriteBuffer() {
        return BYTE_BUFFER.get();
    }

    @Override
    protected void handleBrokenConnection(SocketChannel recipient) {
        BattleServiceConfiguration configuration = BattleServiceConfigurationFactory.getConfiguration();
        BattleServiceContext context = configuration.getContext();
        BattleClient client = context.getClient(recipient);
        if (client != null) {
            logger.debug("close a battle due to problems on socket channel for {}", client.getAccount().getName());
            ServerBattle battle = client.getServerBattle();
            List<SocketChannel> recipients = BattleServiceRequestUtils.getConnectedChannels(BattleServiceRequestUtils.getRecipients(battle.getClients()));
            MicroByteBuffer buffer = new MicroByteBuffer(new byte[128]);
            configuration.getWriter().addMessageToClient(new BattleMessageToClient(recipients, new ServerCloseBattleMessage(buffer, "One or more clients have left a battle").serialize()));
            CommonBattleManager.closeBattle(battle);
        }
        isError = true;
    }

    @Override
    protected boolean worthToSend() {
        return !isError;
    }
}

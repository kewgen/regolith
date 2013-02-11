package com.geargames.regolith.service;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;

/**
 * User: mikhail v. kutuzov
 * Date: 30.08.12
 * Time: 10:39
 */
public class BattleMessageToClient extends MessageToClient {
    public static  ThreadLocal<ByteBuffer> BYTE_BUFFER = new ThreadLocal<ByteBuffer>(){
        @Override
        protected ByteBuffer initialValue() {
            return ByteBuffer.allocateDirect(BattleServiceConfigurationFactory.getConfiguration().getMessageBufferSize());
        }
    };


    public BattleMessageToClient(Collection<SocketChannel> recipients, byte[] message) {
        super(recipients, message);
    }

    @Override
    protected ByteBuffer getWriteBuffer() {
        return BYTE_BUFFER.get();
    }

    @Override
    protected void handleBrokenConnection(SocketChannel recipient) {
    }
}

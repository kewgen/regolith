package com.geargames.regolith.service;

import com.geargames.regolith.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;

/**
 * User: mkutuzov
 * Date: 14.05.12
 */
public abstract class ClientReader {
    private Map<SocketChannel, ByteBuffer> dataTail;
    private ExecutorService messages;

    public ClientReader() {
        dataTail = new HashMap<SocketChannel, ByteBuffer>();
        messages = Executors.newFixedThreadPool(1);
    }

    public abstract int getMessageBufferSize();

    /**
     * Разобрать данные пришедшие в socketChannel по сообщениям и сложить в очередь обработчика сообщений.
     *
     * @param socketChannel
     * @throws IOException
     * @throws AccountIsNotLoggedIn
     */
    public void read(SocketChannel socketChannel) throws Exception {
        ByteBuffer buffer = dataTail.get(socketChannel);
        if (buffer != null) {
            if (buffer.hasRemaining()) {
                buffer.compact();
            } else {
                buffer.clear();
            }
        } else {
            buffer = ByteBuffer.allocate(getMessageBufferSize());
        }
        socketChannel.read(buffer);
        buffer.flip();
        System.out.println("ClientReader: read (" + buffer.limit() + ")");
        putMessages(buffer, socketChannel);
    }

    private void putMessages(ByteBuffer buffer, SocketChannel channel) throws Exception {
        while (buffer.remaining() >= Packets.HEAD_SIZE) {
            buffer.mark();
            short size = buffer.getShort();
            if (buffer.remaining() >= size + Packets.TYPE_FIELD_SIZE) {
                byte[] bytes = new byte[size + Packets.TYPE_FIELD_SIZE];
                buffer.get(bytes);
                execute(messages, bytes, channel);
            } else {
                buffer.reset();
                break;
            }
        }
        dataTail.put(channel, buffer);
    }

    protected abstract void execute(ExecutorService service, byte[] data, SocketChannel channel);
}

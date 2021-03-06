package com.geargames.regolith.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;

/**
 * @author Mikhail_Kutuzov
 *         created: 11.06.12  16:10
 */
public abstract class MessageToClient implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(MessageToClient.class);

    private Collection<SocketChannel> recipients;
    private byte[] message;

    public MessageToClient(Collection<SocketChannel> recipients, byte[] message) {
        this.recipients = recipients;
        this.message = message;
    }

    @Override
    public void run() {
        ByteBuffer byteBuffer = getWriteBuffer();
        byteBuffer.clear();
        byteBuffer.put(message);
        byteBuffer.flip();
        int i = 0;
        for (SocketChannel recipient : recipients) {
            byteBuffer.mark();
            while (byteBuffer.hasRemaining()) {
                try {
                    recipient.write(byteBuffer);
                } catch (IOException e) {
                    handleBrokenConnection(recipient);
                    logger.error("Could not write to a client channel ", e);
                    break;
                }
            }
            if (!worthToSend()) {
                break;
            }
            byteBuffer.reset();
            if (logger.isDebugEnabled()) {
                byteBuffer.mark();
                logger.debug("a sent message length {}", byteBuffer.getShort());
                logger.debug("a type {}", byteBuffer.getShort());
                logger.debug("a recipient number {}", i++);
                byteBuffer.reset();
            }
        }
    }

    protected abstract ByteBuffer getWriteBuffer();

    protected abstract void handleBrokenConnection(SocketChannel recipient);

    protected abstract boolean worthToSend();
}

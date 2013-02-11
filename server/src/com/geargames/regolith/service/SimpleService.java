package com.geargames.regolith.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * User: mkutuzov
 * Date: 14.05.12
 */
public class SimpleService extends ReadingService {
    private static Logger logger = LoggerFactory.getLogger(SimpleService.class);
    private ClientReader reader;

    public SimpleService(int port, ClientReader reader) {
        super(port);
        this.reader = reader;
    }

    @Override
    protected void handleSelectionKey(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            ServerSocket serverSocket = ((ServerSocketChannel) key.channel()).socket();
            Socket socket = serverSocket.accept();
            SocketChannel socketChannel = socket.getChannel();
            socketChannel.configureBlocking(false);
            socketChannel.register(key.selector(), SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            SocketChannel socketChannel = null;
            try {
                socketChannel = (SocketChannel) key.channel();
                reader.read(socketChannel);
            } catch (IOException ie) {
                key.cancel();
                try {
                    if (socketChannel != null) {
                        socketChannel.close();
                    }
                } catch (IOException ie2) {
                    logger.error("could not close a channel ", ie2);
                }
                logger.error("a client channel was closed because of", ie);
            } catch (Exception e) {
                logger.error("an unknown error occurred for a clients channel (DEVELOPER ATTENTION) ", e);
                return;
            }
        }
    }

}

package com.geargames.regolith.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

public abstract class ReadingService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ReadingService.class);
    private int port;
    private volatile boolean run;

    public ReadingService(int port) {
        this.port = port;
        this.run = true;
    }

    public void stopService() {
        this.run = false;
    }

    protected abstract void handleSelectionKey(SelectionKey key) throws IOException;

    public void run() {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();

            ssc.configureBlocking(false);

            ServerSocket serverSocket = ssc.socket();
            InetSocketAddress isa = new InetSocketAddress(port);
            serverSocket.bind(isa);

            Selector selector = Selector.open();

            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (run) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                for (SelectionKey key : keys) {
                    if (key.isValid()) {
                        handleSelectionKey(key);
                    }
                }
                keys.clear();
            }
        } catch (IOException ie) {
            logger.error("an IO error occurred for a client's service (ADMINISTRATOR ATTENTION) ", ie);
        }
    }

}

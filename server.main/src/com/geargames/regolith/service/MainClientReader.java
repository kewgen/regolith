package com.geargames.regolith.service;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

public class MainClientReader extends ClientReader {
    private int messageBufferSize;

    public MainClientReader(){
        messageBufferSize = MainServerConfigurationFactory.getConfiguration().getMessageBufferSize();
    }

    @Override
    public int getMessageBufferSize() {
        return messageBufferSize;
    }

    @Override
    protected void execute(ExecutorService service, byte[] data, SocketChannel channel) {
        service.execute(new MessageFromClientToMainService(channel, data));
    }
}

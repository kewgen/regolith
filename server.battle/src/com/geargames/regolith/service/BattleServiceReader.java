package com.geargames.regolith.service;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

/**
 * User: mikhail v. kutuzov
 * Date: 10.08.12
 * Time: 17:54
 */
public class BattleServiceReader extends ClientReader {
    private int messageBufferSize;

    public BattleServiceReader() {
        messageBufferSize = BattleServiceConfigurationFactory.getConfiguration().getMessageBufferSize();
    }

    @Override
    public int getMessageBufferSize() {
        return messageBufferSize;
    }

    @Override
    protected void execute(ExecutorService service, byte[] data, SocketChannel channel) {
        service.execute(new MessageFromClientToBattleService(channel, data));
    }
}

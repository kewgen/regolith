package com.geargames.regolith.service;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.service.state.ClientNotLoggedInAtBattle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class MessageFromClientToBattleService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(MessageFromClientToBattleService.class);
    private SocketChannel channel;
    private byte[] buffer;
    private BattleServiceConfiguration configuration;

    public MessageFromClientToBattleService(SocketChannel channel, byte[] buffer) {
        this.channel = channel;
        this.buffer = buffer;
        this.configuration = BattleServiceConfigurationFactory.getConfiguration();
    }

    @Override
    public void run() {
        BattleClient client = configuration.getContext().getClient(channel);
        if (client == null) {
            client = new BattleClient();
            client.setChannel(channel);
            client.setState(new ClientNotLoggedInAtBattle());
            configuration.getContext().addClient(channel, client);
        }
        try {
            client.lock();
            client.getState().execute(new MicroByteBuffer(buffer), client);
            client.release();
        } catch (RegolithException e) {
            logger.error("Regolith execution exception", e);
            try {
                client.getChannel().close();
            } catch (IOException e1) {

            }
        }
    }

}

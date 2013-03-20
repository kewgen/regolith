package com.geargames.regolith.service;

import com.geargames.regolith.RegolithException;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.service.states.ClientNotLoggedIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class MessageFromClientToMainService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(MessageFromClientToMainService.class);
    private SocketChannel channel;
    private byte[] buffer;
    private MainServerConfiguration serverConfiguration;

    public MessageFromClientToMainService(SocketChannel channel, byte[] buffer) {
        this.channel = channel;
        this.buffer = buffer;
        this.serverConfiguration = MainServerConfigurationFactory.getConfiguration();
    }

    @Override
    public void run() {
        Client client = serverConfiguration.getServerContext().getClient(channel);
        if (client == null) {
            client = new Client();
            client.setChannel(channel);
            client.setState(new ClientNotLoggedIn());
            serverConfiguration.getServerContext().addClient(channel, client);
        }
        try {
            client.getState().execute(new MicroByteBuffer(buffer), client);
        } catch (RegolithException e) {
            logger.error("Regolith execution exception", e);
            try {
                client.getChannel().close();
            } catch (IOException e1) {

            }
        }
    }
}

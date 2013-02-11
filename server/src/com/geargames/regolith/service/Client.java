package com.geargames.regolith.service;

import com.geargames.regolith.service.clientstates.ClientState;
import com.geargames.regolith.units.Account;

import java.nio.channels.SocketChannel;

/**
 * @author Mikhail_Kutuzov
 *         created: 11.06.12  15:18
 */
public class Client {
    private ClientState state;
    private Account account;
    private SocketChannel channel;

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

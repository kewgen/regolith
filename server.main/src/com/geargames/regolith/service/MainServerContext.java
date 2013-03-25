package com.geargames.regolith.service;

import com.geargames.regolith.service.remote.BattleServiceDescriptor;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;

import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Mikhail_Kutuzov
 *         created: 11.06.12  13:35
 */
public class MainServerContext implements ServerContext {
    private Set<Warrior> warriors;
    private ConcurrentHashMap<String, BattleServiceDescriptor> battleServicePorts;
    private ConcurrentHashMap<String, Integer> battleServicePowers;

    private ConcurrentHashMap<SocketChannel, Client> clients;
    private ConcurrentHashMap<Account, SocketChannel> channels;
    private ConcurrentSkipListMap<Integer, Account> activeAccounts;
    private BattleManagerContext battleManagerContext;

    public MainServerContext(int inputDataProcessorsAmount){
        clients = new ConcurrentHashMap<SocketChannel, Client>(inputDataProcessorsAmount);
        channels = new ConcurrentHashMap<Account, SocketChannel>(inputDataProcessorsAmount);
        activeAccounts = new ConcurrentSkipListMap<Integer, Account>();
        battleServicePorts = new ConcurrentHashMap<String, BattleServiceDescriptor>(1);
        battleServicePowers = new ConcurrentHashMap<String, Integer>(1);
    }

    @Override
    public ConcurrentHashMap<String, Integer> getBattleServicePowers() {
        return battleServicePowers;
    }

    @Override
    public ConcurrentHashMap<String, BattleServiceDescriptor> getBattleServiceSocketPairs() {
        return battleServicePorts;
    }

    public void setBattleManagerContext(BattleManagerContext battleManagerContext) {
        this.battleManagerContext = battleManagerContext;
    }

    @Override
    public Client getClient(SocketChannel channel) {
        return clients.get(channel);
    }

    @Override
    public boolean addClient(SocketChannel channel, Client client) {
        return clients.putIfAbsent(channel, client) == null;
    }

    @Override
    public boolean removeClient(SocketChannel socketChannel) {
        return clients.remove(socketChannel) != null;
    }

    @Override
    public BattleManagerContext getBattleManagerContext() {
        return battleManagerContext;
    }

    @Override
    public SocketChannel getChannel(Account account) {
        return channels.get(account);
    }

    @Override
    public boolean addChannel(Account account, SocketChannel channel) {
        activeAccounts.put(account.getId(), account);
        return channels.putIfAbsent(account, channel) == null;
    }

    @Override
    public boolean removeChannel(Account account) {
        activeAccounts.remove(account.getId());
        return channels.remove(account) != null;
    }

    @Override
    public Account getActiveAccountById(int id) {
        return activeAccounts.get(id);
    }

    @Override
    public synchronized Set<Warrior> getBaseWarriors() {
        return warriors;
    }

    public synchronized void setWarriors(Set<Warrior> warriors) {
        this.warriors = warriors;
    }
}

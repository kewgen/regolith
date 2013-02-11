package com.geargames.regolith.service;

import com.geargames.regolith.service.BattleManagerContext;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.service.remote.BattleServiceDescriptor;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mikhail.kutuzov
 * Date: 10.06.12
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
public interface ServerContext {
    Client getClient(SocketChannel channel);
    boolean addClient(SocketChannel channel, Client client);
    boolean removeClient(SocketChannel socketChannel);

    SocketChannel getChannel(Account account);
    boolean addChannel(Account account, SocketChannel channel);
    boolean removeChannel(Account account);

    Account getActiveAccountById(int id);

    BattleManagerContext getBattleManagerContext();

    Set<Warrior> getBaseWarriors();

    Map<String, BattleServiceDescriptor> getBattleServiceSocketPairs();
    Map<String, Integer> getBattleServicePowers();
}

package com.geargames.regolith.service;

import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBattleServiceContext implements BattleServiceContext {
    private ConcurrentHashMap<SocketChannel, BattleClient> clients;
    private ConcurrentHashMap<Integer, ServerBattle> battles;

    public ConcurrentBattleServiceContext() {
        clients = new ConcurrentHashMap<SocketChannel, BattleClient>(2);
        battles = new ConcurrentHashMap<Integer, ServerBattle>(1);
    }

    @Override
    public BattleClient getClient(SocketChannel channel) {
        return clients.get(channel);
    }

    @Override
    public BattleClient addClient(SocketChannel channel, BattleClient client) {
        return clients.put(channel, client);
    }

    @Override
    public BattleClient removeClient(SocketChannel socketChannel) {
        return clients.remove(socketChannel);
    }

    @Override
    public ServerBattle addBattle(Battle battle, int id) {
        ServerBattle serverBattle = new ServerBattle(battle);
        battles.put(id, serverBattle);

        serverBattle.setGroups(new LinkedList<BattleGroup>());
        serverBattle.setAlliances(new ArrayList<Set<BattleClient>>(serverBattle.getBattle().getBattleType().getAllianceAmount()));
        for (BattleAlliance alliance : battle.getAlliances()) {
            serverBattle.getAlliances().add(new HashSet<BattleClient>());
            for (BattleGroup battleGroup : ((ServerBattleGroupCollection) alliance.getAllies()).getBattleGroups()) {
                serverBattle.getGroups().add(battleGroup);
            }
        }
        serverBattle.setClients(new HashSet<BattleClient>());

        return serverBattle;
    }

    @Override
    public ServerBattle getServerBattleById(int id) {
        return battles.get(id);
    }

    @Override
    public ServerBattle removeServerBattleById(int id) {
        return battles.remove(id);
    }
}

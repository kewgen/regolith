package com.geargames.regolith.service;

import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.ServerBattle;

import java.nio.channels.SocketChannel;

public interface BattleServiceContext {
    BattleClient getClient(SocketChannel channel);
    BattleClient addClient(SocketChannel channel, BattleClient client);
    BattleClient removeClient(SocketChannel socketChannel);

    ServerBattle addBattle(Battle battle, int id);
    ServerBattle getServerBattleById(int id);
    ServerBattle removeServerBattleById(int id);
}

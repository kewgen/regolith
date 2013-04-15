package com.geargames.regolith.service;

import com.geargames.regolith.units.battle.ServerBattle;

import java.util.concurrent.locks.ReentrantLock;

/**
 * User: mikhail v. kutuzov
 * Date: 25.08.12
 * Time: 15:49
 */
public class BattleClient extends Client {
    private ReentrantLock lock;
    private ServerBattle serverBattle;

    public BattleClient() {
        this.lock = new ReentrantLock();
    }

    public void lock(){
        lock.lock();
    }

    public void release(){
        lock.unlock();
    }

    public ServerBattle getServerBattle() {
        return serverBattle;
    }

    public void setServerBattle(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
    }
}

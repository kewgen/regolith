package com.geargames.regolith.service;

import java.util.concurrent.locks.ReentrantLock;

/**
 * User: mikhail v. kutuzov
 * Date: 25.08.12
 * Time: 15:49
 */
public class BattleClient extends Client {
    private ReentrantLock lock;

    public BattleClient() {
        this.lock = new ReentrantLock();
    }

    public void lock(){
        lock.lock();
    }

    public void release(){
        lock.unlock();
    }

}

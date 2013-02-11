package com.geargames.regolith.service.remote;

import com.geargames.regolith.units.battle.Battle;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: mikhail v. kutuzov
 * Date: 10.08.12
 * Time: 11:53
 */
public interface BattleServiceFunctions extends Remote {
    void register(Battle battle) throws RemoteException;
}

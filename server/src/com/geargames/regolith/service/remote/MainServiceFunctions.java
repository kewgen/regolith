package com.geargames.regolith.service.remote;

import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.units.battle.Battle;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * User: mikhail v. kutuzov
 * Date: 10.08.12
 * Time: 11:47
 */
public interface MainServiceFunctions extends Remote {
    RegolithConfiguration register(BattleServiceDescriptor battleServiceDescriptor) throws RemoteException;
    void unRegister(String name)  throws RemoteException;
    void battleComplete(Battle battle, String name) throws RemoteException;
}

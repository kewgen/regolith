package com.geargames.regolith.service.remote;

import com.geargames.regolith.service.BattleServiceConfigurationFactory;
import com.geargames.regolith.units.battle.Battle;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

/**
 * User: mikhail v. kutuzov
 * Date: 10.08.12
 * Time: 12:46
 */
public class BattleServiceRegister extends UnicastRemoteObject implements BattleServiceFunctions {
    @Override
    public void register(Battle battle) throws RemoteException {
        BattleServiceConfigurationFactory.getConfiguration().getContext().addBattle(battle, battle.getId());
    }

    public int getBattlesAmount() throws RemoteException {
        return BattleServiceConfigurationFactory.getConfiguration().getContext().getServerBattlesAmount();
    }

    public BattleServiceRegister() throws RemoteException {
    }

    public BattleServiceRegister(int port) throws RemoteException {
        super(port);
    }

    public BattleServiceRegister(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }
}


package com.geargames.regolith.service.remote;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.service.ServerContext;
import com.geargames.regolith.helpers.ServerContextHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.ServerBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ServerWarriorCollection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

/**
 * User: mikhail v. kutuzov
 * Date: 10.08.12
 * Time: 12:51
 */
public class MainServiceRegister extends UnicastRemoteObject implements MainServiceFunctions {
    @Override
    public RegolithConfiguration register(BattleServiceDescriptor battleServiceDescriptor) throws RemoteException{
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        ServerContextHelper.addBattleService(configuration.getServerContext(),battleServiceDescriptor.getName(), battleServiceDescriptor);
        return configuration.getRegolithConfiguration();
    }

    @Override
    public void unRegister(String name) throws RemoteException {
        ServerContext context = MainServerConfigurationFactory.getConfiguration().getServerContext();
        context.getBattleServiceSocketPairs().remove(name);
        ServerContextHelper.removeBattleService(context, name);
    }

    @Override
    public void battleComplete(Battle battle, String name) throws RemoteException {
        MainServerConfiguration serverConfiguration = MainServerConfigurationFactory.getConfiguration();
        BaseConfiguration baseConfiguration = serverConfiguration.getRegolithConfiguration().getBaseConfiguration();
        SessionFactory sessionFactory = serverConfiguration.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        for(BattleAlliance alliance : battle.getAlliances()){
            for(BattleGroup group : ((ServerBattleGroupCollection)alliance.getAllies()).getBattleGroups()){
                for(Warrior warrior : ((ServerWarriorCollection) group.getWarriors()).getWarriors()){
                    WarriorHelper.adjustSkills(warrior, baseConfiguration);
                    session.update(warrior);
                }
            }
        }
        tx.commit();
        session.close();
    }

    public MainServiceRegister() throws RemoteException {
    }

    public MainServiceRegister(int port) throws RemoteException {
        super(port);
    }

    public MainServiceRegister(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }
}

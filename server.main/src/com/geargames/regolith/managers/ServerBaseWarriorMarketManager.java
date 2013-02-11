package com.geargames.regolith.managers;

import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.BaseWarriorsMarket;
import com.geargames.regolith.units.battle.Warrior;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Set;

/**
 * User: mkutuzov
 * Date: 06.07.12
 */
public class ServerBaseWarriorMarketManager {
    private MainServerConfiguration configuration;

    public ServerBaseWarriorMarketManager() {
        configuration = MainServerConfigurationFactory.getConfiguration();
    }

    public Set<Warrior> browseWarriors() {
        Session session = configuration.getSessionFactory().openSession();
        BaseWarriorsMarket result = (BaseWarriorsMarket) session.load(BaseWarriorsMarket.class, configuration.getBaseWarriorsMarketRevision());
        session.close();

        return result.getWarriors();
    }

    public void hireWarriors(Warrior[] warriors, Account account) {
        Session session = configuration.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        for (Warrior warrior : warriors) {
            save(warrior.getHeadArmor(), session);
            save(warrior.getTorsoArmor(), session);
            save(warrior.getLegsArmor(), session);
            save(warrior.getWeapon(), session);
            save(warrior.getBag(), session);
            save(warrior.getAmmunitionBag(), session);
            session.save(warrior);
            account.getWarriors().add(warrior);
        }
        session.merge(account);
        tx.commit();
        session.close();
    }

    private void save(Object object, Session session) {
        if (object != null) {
            session.save(object);
        }
    }

}

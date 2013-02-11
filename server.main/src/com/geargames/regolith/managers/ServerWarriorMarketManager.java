package com.geargames.regolith.managers;

import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.service.MainServerConfigurationFactory;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import org.hibernate.Session;

/**
 * User: mkutuzov
 * Date: 02.07.12
 */
public class ServerWarriorMarketManager {

    public WarriorCollection browseWarriors() {
        MainServerConfiguration configuration = MainServerConfigurationFactory.getConfiguration();
        Session session = configuration.getSessionFactory().openSession();




        session.close();
        return null;
    }

    public boolean hireWarrior(Warrior warrior, Account account) {

        return false;
    }
}

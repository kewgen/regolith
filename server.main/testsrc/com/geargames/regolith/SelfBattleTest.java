package com.geargames.regolith;

import com.geargames.regolith.units.BaseWarriorsMarket;
import com.geargames.regolith.units.map.BattleMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * User: mvkutuzov
 * Date: 18.04.13
 * Time: 11:50
 */
public class SelfBattleTest {
    private static SessionFactory sessionFactory;

    @BeforeClass
    public static void before() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration = configuration.configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
    }

    @Test
    public void oneONone() {
        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        BattleConfiguration battleConfiguration = (BattleConfiguration) session.createQuery("from BattleConfiguration").list().get(0);
        BaseWarriorsMarket baseWarriorsMarket = (BaseWarriorsMarket)session.createQuery("from BaseWarriorsMarket").list().get(0);
        List<BattleMap> battleMaps = session.createQuery("from BattleMap").list();
        session.close();

        //вот тут расставим на map солдатиков и постреляем


    }

}

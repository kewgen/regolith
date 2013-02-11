package com.geargames.regolith;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.BaseWarriorsMarket;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.base.StoreHouseHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.tackle.Projectile;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.Weapon;
import junit.framework.Assert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * User: mikhail v. kutuzov
 * Date: 17.12.12
 * Time: 11:44
 */
public class TackleMovementTest {
    private static SessionFactory sessionFactory;

    @BeforeClass
    public static void before() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration = configuration.configure("hibernate.cfg.xml");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        sessionFactory = configuration.buildSessionFactory();
    }

    @Test
    public void bagToStoreHouse() {
        Session session = sessionFactory.openSession();
        BaseConfiguration baseConfiguration = (BaseConfiguration) session.createQuery("from BaseConfiguration").list().get(0);
        BaseWarriorsMarket market = (BaseWarriorsMarket) session.createCriteria(BaseWarriorsMarket.class).list().get(0);
        session.close();

        Warrior[] warriors = market.getWarriors().toArray(new Warrior[2]);

        Warrior warrior = warriors[0];
        Account account = MainServerHelper.createDefaultAccount(baseConfiguration, "", "");


        int total = WarriorHelper.getTotalLoad(warrior);
        Bag bag = warrior.getBag();

        StoreHouse storeHouse = account.getBase().getStoreHouse();

        Weapon weapon = warrior.getWeapon();
        WarriorHelper.takeOffIntoBag(warrior, weapon);

        int weight = bag.getWeight();
        StateTackle tackle = WarriorHelper.putOutOfBag(warrior, bag.getTackles().size()-1);
        Assert.assertEquals(weight - tackle.getWeight(), bag.getWeight());
        Assert.assertEquals(total - tackle.getWeight(), WarriorHelper.getTotalLoad(warrior));

        Assert.assertTrue(tackle instanceof Weapon);
        int size = StoreHouseHelper.getSize(storeHouse);
        Assert.assertTrue(StoreHouseHelper.add(storeHouse, tackle));
        int sz = StoreHouseHelper.getSize(storeHouse);
        Assert.assertEquals(size + 1, sz);

        size = StoreHouseHelper.getSize(storeHouse);
        tackle = StoreHouseHelper.removeStateTackle(storeHouse, size - 1);

        weight = bag.getWeight();
        int  oldWeight = weight;
        Assert.assertTrue(WarriorHelper.putInToBag(warrior, tackle, baseConfiguration));
        Assert.assertEquals(weight + tackle.getWeight(), bag.getWeight());

        weight = bag.getWeight();
        tackle = WarriorHelper.putOutOfBag(warrior, bag.getTackles().size()-1);
        Assert.assertEquals(weight - tackle.getWeight(), bag.getWeight());
        Assert.assertEquals(total - tackle.getWeight(), WarriorHelper.getTotalLoad(warrior));


        Assert.assertTrue(WarriorHelper.putInToBag(warrior, tackle, baseConfiguration));
        StateTackle stateTackle = WarriorHelper.putOutOfBag(warrior, bag.getTackles().size()-1);

        Assert.assertEquals(tackle, stateTackle);
        Assert.assertEquals(oldWeight, bag.getWeight());

        total = WarriorHelper.getTotalLoad(warrior);
        Assert.assertTrue(WarriorHelper.takeOn(warrior, tackle, baseConfiguration));

        Assert.assertEquals(total + tackle.getWeight(), WarriorHelper.getTotalLoad(warrior));

        total = WarriorHelper.getTotalLoad(warrior);
        if(tackle instanceof Weapon){
            System.out.println("We have a weapon");
            if(((Weapon) tackle).getProjectile() != null){
                weapon = (Weapon) tackle;
                Projectile projectile = weapon.getProjectile();
                System.out.println("It is loaded with projectiles " + projectile.getName() + ":" + weapon.getLoad());
                int amount = WarriorHelper.addProjectileIntoWeapon(warrior, projectile, 20, baseConfiguration);
                int added = (20 - amount)*projectile.getWeight();
                Assert.assertEquals(total + added, WarriorHelper.getTotalLoad(warrior));
            }
        }
    }

}

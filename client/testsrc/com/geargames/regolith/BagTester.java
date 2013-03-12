package com.geargames.regolith;

import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.AmmunitionBagHelper;
import com.geargames.regolith.units.tackle.Projectile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * User: mkutuzov
 * Date: 18.02.12
 * Time: 21:11
 */
public class BagTester {

    private BaseConfiguration baseConfiguration;

    @Before
    public void before(){
        baseConfiguration = new BaseConfiguration();
        baseConfiguration.setPocketsAmount((byte)10);
    }


    @Test
    public void projectileTest(){
        AmmunitionBag bag = ClientHelper.createAmmunitionBag(baseConfiguration);
        Projectile projectile = new Projectile();
            projectile.setWeight((short)10);
            projectile.setName("патрон" + 1);
        AmmunitionBagHelper.putIn(bag, projectile, 100);
        Projectile projectile1 = new Projectile();
            projectile1.setWeight((short)10);
            projectile1.setName("патрон" + 2);
        AmmunitionBagHelper.putIn(bag, projectile1, 200);
        projectile = new Projectile();
            projectile.setWeight((short)10);
            projectile.setName("патрон" + 3);
        AmmunitionBagHelper.putIn(bag, projectile, 50);

        AmmunitionBagHelper.putIn(bag, projectile1, 20);

        Assert.assertEquals("Забитых карманов число не верно", 5, bag.getSize());
        Assert.assertEquals("Вес не верен сумки", 370, bag.getWeight());

        AmmunitionBagHelper.putOut(bag, projectile, 50);
        Assert.assertEquals("Забитых карманов число не верно", 4, bag.getSize());
        Assert.assertEquals("Вес не верен сумки", 320, bag.getWeight());

    }
}

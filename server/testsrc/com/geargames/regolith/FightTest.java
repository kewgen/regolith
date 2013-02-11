package com.geargames.regolith;

import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.AmmunitionBagHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.dictionaries.ServerProjectileCollection;
import com.geargames.regolith.units.dictionaries.ServerWeaponTypeCollection;
import com.geargames.regolith.units.dictionaries.WeaponTypeCollection;
import com.geargames.regolith.units.tackle.Weapon;
import com.geargames.regolith.units.tackle.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * User: mkutuzov
 * Date: 20.03.12
 */
public class FightTest {
    private WeaponCategory weaponCategory;
    private ServerProjectileCollection faithul;
    private WeaponTypeCollection weaponTypes;
    private Warrior warrior;

    @Before
    public void before(){
        warrior = new Warrior();
        Weapon weapon = new Weapon();

        weaponCategory = new WeaponCategory();
        weaponTypes = new ServerWeaponTypeCollection();
        weaponCategory.setWeaponTypes(weaponTypes);

        faithul = new ServerProjectileCollection();
        faithul.setProjectiles(new ArrayList<Projectile>(3));

        AmmunitionCategory ammunitionCategory1 = new AmmunitionCategory();
        ammunitionCategory1.setName("сексуальные патрончики");
        ammunitionCategory1.setQuality(1.2);

        AmmunitionCategory ammunitionCategory2 = new AmmunitionCategory();
        ammunitionCategory2.setName("клёвые патрончики");
        ammunitionCategory2.setQuality(1.1);

        for(int i=0; i < 3; i++ ){
            Projectile projectile = new Projectile();
            faithul.add(projectile);
            projectile.setWeaponTypes(weaponTypes);
            projectile.setWeight((short)10);
            projectile.setName("патрон" + i);
            if(i%2!=0){
                projectile.setCategory(ammunitionCategory1);
            }else{
                projectile.setCategory(ammunitionCategory2);
            }
        }

        for (int i = 0; i < weaponTypes.size(); i++) {
            WeaponType weaponType = new WeaponType();
            weaponType.setProjectiles(faithul);
            weaponType.setCategory(weaponCategory);
            weaponType.setName("пушка" + i);
            weaponType.setProjectiles(faithul);
            weaponType.setCapacity((short) 50);
            weaponTypes.set(weaponType, i);
        }
        weapon.setWeaponType(weaponTypes.get(0));
        warrior.setWeapon(weapon);

        BaseConfiguration baseConfiguration = ServerTestConfigurationFactory.getDefaultConfiguration().getBaseConfiguration();

        AmmunitionBag bag = ServerHelper.createAmmunitionBag(baseConfiguration);
        warrior.setAmmunitionBag(bag);
        //[100]/[100][100]/[50]
        AmmunitionBagHelper.putIn(bag, faithul.get(0), 100);
        AmmunitionBagHelper.putIn(bag, faithul.get(1), 200);
        AmmunitionBagHelper.putIn(bag, faithul.get(2), 50);
    }

    @Test
    public void rechargeWeapon(){
        Weapon weapon = warrior.getWeapon();
        weapon.setProjectile(faithul.get(0));
        weapon.setLoad((short)20);
        WarriorHelper.rechargeWeapon(warrior, faithul.get(1));

        Assert.assertEquals(weapon.getProjectile(), faithul.get(1));
        Assert.assertEquals(weapon.getLoad(), weapon.getWeaponType().getCapacity());

        weapon.setLoad((short)0);
        WarriorHelper.rechargeWeapon(warrior, faithul.get(1));
        Assert.assertEquals(weapon.getProjectile(), faithul.get(1));
        Assert.assertEquals(weapon.getLoad(), weapon.getWeaponType().getCapacity());

        weapon.setLoad((short)0);
        WarriorHelper.rechargeWeapon(warrior, faithul.get(1));
        Assert.assertEquals(weapon.getProjectile(), faithul.get(1));
        Assert.assertEquals(weapon.getLoad(), weapon.getWeaponType().getCapacity());

        weapon.setLoad((short)0);
        WarriorHelper.rechargeWeapon(warrior, faithul.get(1));
        Assert.assertEquals(weapon.getProjectile(), faithul.get(1));
        Assert.assertEquals(weapon.getLoad(), weapon.getWeaponType().getCapacity());

        weapon.setLoad((short)0);
        WarriorHelper.rechargeWeapon(warrior, faithul.get(1));
        Assert.assertEquals(weapon.getProjectile(), faithul.get(1));
        Assert.assertEquals(weapon.getLoad(), 0);

        WarriorHelper.rechargeWeapon(warrior, faithul.get(2));
        Assert.assertEquals(weapon.getProjectile(), faithul.get(2));
        Assert.assertEquals(weapon.getLoad(), weapon.getWeaponType().getCapacity());
    }
}

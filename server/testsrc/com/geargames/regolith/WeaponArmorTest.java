package com.geargames.regolith;

import com.geargames.regolith.helpers.ArmorHelper;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Weapon;
import com.geargames.regolith.units.tackle.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * User: mkutuzov
 * Date: 13.03.12
 */
public class WeaponArmorTest {
    private static byte DAMAGE = 10;

    @BeforeClass
    public static void befor(){
        ServerTestConfigurationFactory.getDefaultConfiguration();
    }

    @Test
    public void weaponTest(){
        Weapon weapon =  new Weapon();
        WeaponType type = new WeaponType();
        WeaponDistances weaponDistances = new WeaponDistances();
        weaponDistances.setMin((byte)1);
        weaponDistances.setMinOptimal((byte)5);
        weaponDistances.setMaxOptimal((byte)10);
        weaponDistances.setMax((byte)14);
        type.setDistance(weaponDistances);
        WeaponDamage min = new WeaponDamage();
        min.setMaxDistance((byte)1);
        min.setOptDistance((byte)5);
        min.setMinDistance((byte)1);

        WeaponDamage max = new WeaponDamage();
        max.setMaxDistance((byte)2);
        max.setOptDistance((byte)10);
        max.setMaxDistance((byte)2);

        type.setMaxDamage(max);
        type.setMinDamage(min);

        Warrior warrior0 = new Warrior();
        warrior0.setX((short)0);
        warrior0.setY((short)0);
        Warrior warrior1 = new Warrior();
        warrior1.setX((short)3);
        warrior1.setY((short)4);
    }

    @Test
    public void armorTest(){
        Armor armor = new Armor();
        ArmorType type = new ArmorType();
        type.setArmor((byte)50);
        armor.setArmorType(type);
        armor.setState((byte)100);
        armor.setFirmness((byte)100);
        armor.setUpgrade((byte)0);

        int damage = ArmorHelper.getArmorDamageInfluence(armor, DAMAGE, ServerTestConfigurationFactory.getDefaultConfiguration().getBaseConfiguration());
        Assert.assertEquals("первое попадение", 5 , damage);
        armor.onHit(DAMAGE, ServerTestConfigurationFactory.getDefaultConfiguration().getBattleConfiguration());
        Assert.assertEquals("ухудшение состояния брони", 98 , armor.getState());
    }

}

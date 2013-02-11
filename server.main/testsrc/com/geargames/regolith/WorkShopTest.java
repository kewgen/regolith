package com.geargames.regolith;

import com.geargames.regolith.units.base.ServerBaseHelper;
import com.geargames.regolith.units.base.WorkShop;
import com.geargames.regolith.units.tackle.Weapon;
import com.geargames.regolith.units.tackle.WeaponType;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: mkutuzov
 * Date: 13.03.12
 */
public class WorkShopTest {
    private WorkShop workShop;

    @Before
    public void befor(){
        workShop = new WorkShop();
        workShop.setLevel((byte)5);
    }

    @Test
    public void nextUpgrade(){
       int i = 0;
       int j = 0;
       for (int k = 0; k < 1000; k++) {
           if( ServerBaseHelper.getNextLevel(workShop, (byte) 4) == 1 ) {
               i++;
           } else {
               j++;
           }
       }
       // 13 <15 - 10*4/9 < 14
        Assert.assertTrue("количество провалов определено на большом количестве повторений", 14 > i*100.0/1000.0);
    }

    @Test
    public void fixWeapon(){
        Weapon weapon = new Weapon();
        WeaponType type = new WeaponType();
        type.setBaseFirmness((short)100);
        weapon.setWeaponType(type);
        weapon.setFirmness((short)100);
        weapon.setState((short)60);
        ServerBaseHelper.fix(workShop, weapon, ServerTestConfigurationFactory.getDefaultConfiguration());

        Assert.assertEquals("предмет должен быть отремонтирован с потерей прочности", 98 ,weapon.getFirmness());
        Assert.assertEquals("предмет должен быть отремонтирован до состояния наименьшего износа прочности", weapon.getFirmness() , weapon.getState());
    }

    @Test
    public void upgradeArmor(){

    }

}

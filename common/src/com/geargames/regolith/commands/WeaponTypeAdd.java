package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.tackle.WeaponCategory;
import com.geargames.regolith.units.tackle.WeaponType;

/**
 * User: mkutuzov
 * Date: 27.04.12
 */
public class WeaponTypeAdd extends BaseConfigurationCommand {
    private WeaponType weaponType;

    public WeaponTypeAdd(WeaponType weaponType){
        this.weaponType = weaponType;
    }

    public void command(BaseConfiguration configuration) {
        weaponType.getCategory().getWeaponTypes().add(weaponType);
    }
}

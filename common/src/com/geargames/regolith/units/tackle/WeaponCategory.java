package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.dictionaries.WeaponTypeCollection;

/**
 * User: mkutuzov
 * Date: 03.02.12
 */
public class WeaponCategory extends Entity {
    private String name;
    private WeaponTypeCollection weaponTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WeaponTypeCollection getWeaponTypes() {
        return weaponTypes;
    }

    public void setWeaponTypes(WeaponTypeCollection weaponTypes) {
        this.weaponTypes = weaponTypes;
    }
}

package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.ElementTypes;
import com.geargames.regolith.units.dictionaries.WeaponTypeCollection;

/**
 * User: mkutuzov
 * Date: 06.02.12
 */
public class Projectile extends Ammunition {
    private WeaponTypeCollection weaponTypes;

    public WeaponTypeCollection getWeaponTypes() {
        return weaponTypes;
    }

    public void setWeaponTypes(WeaponTypeCollection weaponTypes) {
        this.weaponTypes = weaponTypes;
    }

    public int getType() {
        return TackleType.PROJECTILE;
    }

    @Override
    public short getElementType() {
        return ElementTypes.PROJECTILE;
    }
}

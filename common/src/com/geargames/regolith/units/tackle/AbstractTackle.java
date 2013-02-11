package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.Element;

/**
 * User: mkutuzov
 * Date: 12.03.12
 */
public abstract class AbstractTackle extends Element {

    /**
     * Вернуть тип аммуниции.
     * @return
     */
    public abstract int getType();

    /**
     * Вернуть наменование клади.
     * @return
     */
    public abstract String getName();

    /**
     * Вернуть вес клади.
      * @return
     */
    public abstract short getWeight();

    public boolean isAbleToLookThrough() {
        return true;
    }

    public boolean isAbleToWalkThrough() {
        return true;
    }

    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return true;
    }

    public boolean isHalfLong() {
        return true;
    }
}

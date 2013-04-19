package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.CellElementLayers;

/**
 * User: mkutuzov
 * Date: 12.03.12
 */
public abstract class AbstractTackle extends CellElement {

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

    @Override
    public boolean isAbleToLookThrough() {
        return true;
    }

    @Override
    public boolean isAbleToWalkThrough() {
        return true;
    }

    @Override
    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return true;
    }

    @Override
    public boolean isHalfLong() {
        return true;
    }

    @Override
    public byte getLayer() {
        return CellElementLayers.TACKLE;
    }

}

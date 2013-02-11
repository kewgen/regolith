package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.WeaponType;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public abstract class WeaponTypeCollection extends EntityCollection {
    public abstract WeaponType get(int index);
    public abstract void add(WeaponType weaponType);
    public abstract void insert(WeaponType weaponType, int index);
    public abstract void set(WeaponType weaponType, int index);
}

package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public abstract class WeaponCategoryCollection extends EntityCollection {
    public abstract WeaponCategory get(int index);
    public abstract void add(WeaponCategory weaponCategory);
    public abstract void insert(WeaponCategory weaponCategory, int index);
    public abstract void set(WeaponCategory weaponCategory, int index);
}

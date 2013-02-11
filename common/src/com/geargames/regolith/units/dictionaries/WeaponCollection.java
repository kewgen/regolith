package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Weapon;

/**
 * @author Mikhail_Kutuzov
 *         created: 30.05.12  11:50
 */
public abstract class WeaponCollection extends EntityCollection {
    public abstract Weapon get(int index);
    public abstract void add(Weapon weapon);
    public abstract void insert(Weapon weapon, int index);
    public abstract void set(Weapon weapon, int index);
}

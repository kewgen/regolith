package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Armor;

/**
 * @author Mikhail_Kutuzov
 *         created: 30.05.12  11:54
 */
public abstract class ArmorCollection extends EntityCollection {
    public abstract Armor get(int index);
    public abstract void add(Armor armor);
    public abstract void insert(Armor armor, int index);
    public abstract void set(Armor armor, int index);
}

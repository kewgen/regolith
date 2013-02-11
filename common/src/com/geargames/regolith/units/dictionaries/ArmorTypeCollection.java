package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.ArmorType;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public abstract class ArmorTypeCollection extends EntityCollection {
    public abstract ArmorType get(int index);
    public abstract void add(ArmorType armorType);
    public abstract void insert(ArmorType armorType, int index);
    public abstract void set(ArmorType armorType, int index);
}

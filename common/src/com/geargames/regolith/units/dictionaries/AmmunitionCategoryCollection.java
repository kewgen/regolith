package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.AmmunitionCategory;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public abstract class AmmunitionCategoryCollection extends EntityCollection {
    public abstract AmmunitionCategory get(int index);
    public abstract void add(AmmunitionCategory category);
    public abstract void insert(AmmunitionCategory category, int index);
    public abstract void set(AmmunitionCategory category, int  index);
}

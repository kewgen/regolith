package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Border;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public abstract class BorderCollection extends EntityCollection {
    public abstract Border get(int index);
    public abstract void add(Border border);
    public abstract void insert(Border border, int index);
    public abstract void set(Border border, int index);
}

package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.battle.Ally;

/**
 * User: mikhail v. kutuzov
 * Date: 22.08.12
 * Time: 15:25
 */
public abstract class AllyCollection extends EntityCollection {
    public abstract Ally get(int index);
    public abstract void add(Ally ally);
    public abstract void insert(Ally ally, int index);
    public abstract void set(Ally ally, int index);
    public abstract void clear();
}


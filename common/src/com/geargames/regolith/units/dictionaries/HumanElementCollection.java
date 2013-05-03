package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.map.HumanElement;

/**
 * User: abarakov
 * Date: 30.04.13
 */
public abstract class HumanElementCollection extends EntityCollection {
    public abstract HumanElement get(int index);

    public abstract void add(HumanElement item);

    public abstract void addAll(HumanElementCollection collection);

    public abstract void insert(HumanElement item, int index);

    public abstract void set(HumanElement item, int index);

    public abstract void clear();
}


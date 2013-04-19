package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Barrier;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public abstract class BarrierCollection extends EntityCollection {
    public abstract Barrier get(int index);
    public abstract void add(Barrier barrier);
    public abstract void insert(Barrier barrier, int index);
    public abstract void set(Barrier barrier, int index);
}

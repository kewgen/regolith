package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Rank;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public abstract class RankCollection extends EntityCollection {
    public abstract Rank get(int index);
    public abstract void add(Rank rank);
    public abstract void insert(Rank rank, int index);
    public abstract void set(Rank rank, int index);
}

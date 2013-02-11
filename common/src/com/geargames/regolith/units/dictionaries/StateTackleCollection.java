package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.AbstractTackle;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * @author Mikhail_Kutuzov
 *         created: 29.05.12  23:18
 */
public abstract class StateTackleCollection extends EntityCollection {
    public abstract StateTackle get(int index);
    public abstract void add(StateTackle stateTackle);
    public abstract void insert(StateTackle stateTackle, int index);
    public abstract void set(StateTackle stateTackle, int index);
}

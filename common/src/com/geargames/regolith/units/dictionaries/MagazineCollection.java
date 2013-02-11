package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Magazine;

/**
 * @author Mikhail_Kutuzov
 *         created: 29.05.12  23:22
 */
public abstract class MagazineCollection extends EntityCollection {
    public abstract Magazine get(int index);
    public abstract void add(Magazine magazine);
    public abstract void insert(Magazine magazine, int index);
    public abstract void set(Magazine magazine, int index);
}

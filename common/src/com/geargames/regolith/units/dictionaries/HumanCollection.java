package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.battle.Ally;

/**
 * User: mikhail v. kutuzov
 * Date: 23.08.12
 * Time: 11:36
 */
public abstract class HumanCollection extends EntityCollection {
    public abstract Human get(int index);
    public abstract void add(Human human);
    public abstract void insert(Human human, int index);
    public abstract void set(Human human, int index);
    public abstract void clear();
}

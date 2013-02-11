package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Battle;

/**
 * User: mikhail v. kutuzov
 * Коллекция из битв.
 */
public abstract class BattleCollection extends EntityCollection {
    public abstract Battle get(int index);
    public abstract void add(Battle battle);
    public abstract void insert(Battle battle, int index);
    public abstract void set(Battle battle, int index);
}

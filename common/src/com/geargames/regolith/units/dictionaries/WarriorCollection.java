package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Warrior;

/**
 * Users: mkutuzov, abarakov
 * Date: 02.05.12
 */
public abstract class WarriorCollection extends EntityCollection {

    public abstract Warrior get(int index);

    public abstract void add(Warrior warrior);

    public abstract void addAll(WarriorCollection collection);

    public abstract void insert(Warrior warrior, int index);

    public abstract void set(Warrior warrior, int index);

    public abstract boolean contains(Warrior warrior);

    public abstract void clear();

}

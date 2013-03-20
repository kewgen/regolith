package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.BattleType;

/**
 * User: mkutuzov
 * Date: 12.05.12
 * Коллекция боевых групп.
 */
public abstract class BattleGroupCollection extends EntityCollection {

    public abstract BattleGroup get(int index);
    public abstract void add(BattleGroup battleGroup);
    public abstract void insert(BattleGroup battleGroup, int index);
    public abstract void set(BattleGroup battleGroup, int index);

}

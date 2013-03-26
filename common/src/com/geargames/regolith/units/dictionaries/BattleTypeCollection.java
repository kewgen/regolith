package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.BattleType;

/**
 * @author Mikhail_Kutuzov
 *         created: 11.05.12  12:29
 */
public abstract class BattleTypeCollection extends EntityCollection {
    public abstract BattleType get(int index);
    public abstract void add(BattleType battleType);
    public abstract void insert(BattleType battleType, int index);
    public abstract void set(BattleType battleType, int index);
}

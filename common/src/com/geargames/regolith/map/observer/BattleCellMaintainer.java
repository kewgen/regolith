package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public abstract class BattleCellMaintainer {

    public abstract boolean maintain(BattleCell[][] cells, Warrior warrior, boolean toDo, int x, int y);

}

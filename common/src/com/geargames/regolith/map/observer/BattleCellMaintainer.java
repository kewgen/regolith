package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.HumanElement;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public abstract class BattleCellMaintainer {

    public abstract boolean maintain(BattleCell[][] cells, HumanElement unit, boolean toDo, int x, int y);

}

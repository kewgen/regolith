package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public abstract class ViewCaster {

    public abstract void castViewUp(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer maintainer);

    public abstract void castViewDown(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer maintainer);

    public abstract void castViewLeft(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer maintainer);

    public abstract void castViewRight(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer maintainer);

}

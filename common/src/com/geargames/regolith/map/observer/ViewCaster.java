package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public abstract class ViewCaster {

    public abstract void castViewToLesserY(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer maintainer);

    public abstract void castViewToBiggerY(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer maintainer);

    public abstract void castViewToLesserX(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer maintainer);

    public abstract void castViewToBiggerX(int x0, int y0, int x1, int y1, BattleMap battleMap, Warrior warrior, BattleCellMaintainer maintainer);

}

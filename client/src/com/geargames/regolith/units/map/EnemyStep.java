package com.geargames.regolith.units.map;


import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mvkutuzov
 * Date: 24.04.13
 * Time: 10:56
 */
public class EnemyStep extends Step {

    @Override
    protected void routeMap(Warrior warrior) {
    }

    @Override
    protected boolean hasToStop(Warrior warrior) {
        return false;
    }

    @Override
    protected void doStepOnMap(Warrior warrior, int stepX, int stepY) {
        BattleMap map = getBattleMap();
        BattleMapHelper.resetShortestCell(map.getCells()[warrior.getX()][warrior.getY()], getAlliance(), warrior);
        WarriorHelper.putWarriorIntoMap(warrior, map, warrior.getX() + stepX, warrior.getY() + stepY);
    }

}

package com.geargames.regolith.map.detectors;

import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMapHelper;
import com.geargames.regolith.units.battle.Ally;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 24.02.12
 */
public class StopFoundEnemy extends UnitDetector {

    private static StopFoundEnemy instance = new StopFoundEnemy();

    private StopFoundEnemy() {
    }

    public void detected(Ally warrior, BattleCell cell) {
        if(!BattleMapHelper.isVisible(cell, warrior.getBattleGroup().getAlliance())
                && cell.getElement() != null
                && cell.getElement() instanceof Warrior) {
            if(((Warrior)(cell.getElement())).getBattleGroup().getAlliance() != warrior.getBattleGroup().getAlliance()){
                warrior.setMoving(false);
            }
        }
    }

    public static StopFoundEnemy getInstance() {
        return instance;
    }

}

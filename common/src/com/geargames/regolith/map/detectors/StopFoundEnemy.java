package com.geargames.regolith.map.detectors;

import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.CellElementTypes;

/**
 * Users: mkutuzov, abarakov
 * Date: 24.02.12
 */
public class StopFoundEnemy extends UnitDetector {
    private static StopFoundEnemy instance = new StopFoundEnemy();

    private StopFoundEnemy() {
    }

    public void detected(Warrior warrior, BattleCell cell) {
        if (!BattleMapHelper.isVisible(cell, warrior.getBattleGroup().getAlliance())
                && cell.getElement() != null
                && cell.getElement().getElementType() == CellElementTypes.HUMAN) {
            Warrior warriorElement = (Warrior) cell.getElement();
            if (/*warriorElement.getMembershipType() == Human.ALLY && */warriorElement.getBattleGroup().getAlliance() != warrior.getBattleGroup().getAlliance()) {
//                warrior.setMoving(false); //todo: Правильно остановить бойца
            }
        }
    }

    public static StopFoundEnemy getInstance() {
        return instance;
    }

}

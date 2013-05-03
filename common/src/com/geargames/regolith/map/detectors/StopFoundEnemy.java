package com.geargames.regolith.map.detectors;

import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.CellElementTypes;
import com.geargames.regolith.units.map.HumanElement;

/**
 * Users: mkutuzov, abarakov
 * Date: 24.02.12
 */
public class StopFoundEnemy extends UnitDetector {
    private static StopFoundEnemy instance = new StopFoundEnemy();

    private StopFoundEnemy() {
    }

    public void detected(HumanElement unit, BattleCell cell) {
        if (!BattleMapHelper.isVisible(cell, unit.getHuman().getBattleGroup().getAlliance())
                && cell.getElement() != null
                && cell.getElement().getElementType() == CellElementTypes.HUMAN) {
            Human humanElement = ((HumanElement) cell.getElement()).getHuman();
            if (humanElement.getMembershipType() == Human.ALLY && humanElement.getBattleGroup().getAlliance() != unit.getHuman().getBattleGroup().getAlliance()) {
//                unit.setMoving(false); //todo: ?????
            }
        }
    }

    public static StopFoundEnemy getInstance() {
        return instance;
    }

}

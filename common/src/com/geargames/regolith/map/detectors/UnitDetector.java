package com.geargames.regolith.map.detectors;

import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;

/**
 * User: mkutuzov
 * Date: 24.02.12
 */
public abstract class UnitDetector {

    public abstract void detected(Warrior warrior, BattleCell cell);

}

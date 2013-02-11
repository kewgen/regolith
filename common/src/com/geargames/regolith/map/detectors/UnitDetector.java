package com.geargames.regolith.map.detectors;

import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.battle.Ally;

/**
 * User: mkutuzov
 * Date: 24.02.12
 */
public abstract class UnitDetector {
    public abstract void detected(Ally warrior, BattleCell cell);
}

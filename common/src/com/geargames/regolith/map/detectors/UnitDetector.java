package com.geargames.regolith.map.detectors;

import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.HumanElement;

/**
 * User: mkutuzov
 * Date: 24.02.12
 */
public abstract class UnitDetector {
    public abstract void detected(HumanElement unit, BattleCell cell);
}

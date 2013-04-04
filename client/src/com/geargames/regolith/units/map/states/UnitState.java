package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.common.packer.PUnitScript;
import com.geargames.regolith.units.map.Unit;

/**
 * User: m.v.kutuzov
 * Date: 02.04.13
 */
public abstract class UnitState {
    protected abstract PUnitScript getPUnitScript(Render render, Unit unit, int action);
    public abstract void next();
}

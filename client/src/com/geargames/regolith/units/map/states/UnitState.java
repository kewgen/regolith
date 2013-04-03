package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.BattleUnit;

/**
 * User: m.v.kutuzov
 * Date: 02.04.13
 */
public abstract class UnitState {
    public abstract void init(Render render, byte base);
    public abstract PUnit getPUnit(BattleUnit unit, Render render);
    public abstract void onTime();
}

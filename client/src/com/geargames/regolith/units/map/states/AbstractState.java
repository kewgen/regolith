package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.common.packer.PUnit;
import com.geargames.common.packer.PUnitScript;
import com.geargames.regolith.helpers.ClientGUIHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public abstract class AbstractState extends UnitState {
    protected byte current;
    protected byte limit;
    protected PUnitScript defaultScript;

    public PUnit getPUnit(Render render, Unit unit, int action) {
        PUnitScript script = getPUnitScript(render, unit, action);
        return (PUnit) script.getIndex(current).getPrototype();
    }

    public void init(Render render, Unit unit, int action) {
        Warrior warrior = unit.getWarrior();
        defaultScript = render.getUnitScript(warrior.getWeapon().getWeaponType().getCapacity() + action + ClientGUIHelper.getPackerScriptsDirection(warrior.getDirection()));
        current = 0;
        limit = (byte)defaultScript.getIndexes().size();
    }
}

package com.geargames.regolith.units.map.states;

import com.geargames.common.env.Environment;
import com.geargames.common.packer.PUnitScript;
import com.geargames.regolith.helpers.ClientGUIHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public abstract class AbstractWarriorState extends UnitState {
    protected byte current;
    protected byte limit;
    protected PUnitScript defaultScript;

    /**
     * Перейти на новый PUnitScript для отображения бойца.
     * @param unit
     * @param state
     * @return
     */
    public abstract void change(Unit unit, AbstractWarriorState state);

    public void init(Unit unit) {
        Warrior warrior = unit.getWarrior();
        defaultScript = Environment.getRender().getUnitScript(warrior.getWeapon().getWeaponType().getCapacity() + getAction() + ClientGUIHelper.getPackerScriptsDirection(warrior.getDirection()));
        current = 0;
        limit = (byte)defaultScript.getIndexes().size();
        unit.setState(this);
    }

    public abstract byte getAction();
}

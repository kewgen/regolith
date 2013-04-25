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
    protected byte current;              // номер шага анимации
    protected byte limit;                // количество шагов анимации
    protected PUnitScript defaultScript;
    private UnitFinishListener listener;

    /**
     * Перейти на новый PUnitScript для отображения бойца.
     * @param unit
     * @param state
     * @return
     */
    public abstract void change(Unit unit, AbstractWarriorState state);

    public UnitFinishListener getListener() {
        return listener;
    }

    public void setListener(UnitFinishListener listener) {
        this.listener = listener;
    }

    public void init(Unit unit) {
        Warrior warrior = unit.getWarrior();
        defaultScript = Environment.getRender().getUnitScript(warrior.getWeapon().getWeaponType().getCategory().getPackerId() + getAction() + ClientGUIHelper.convertPackerScriptsDirection(warrior.getDirection()));
        current = 0;
        limit = (byte) defaultScript.getIndexes().size();
        unit.setState(this);
    }

    public abstract byte getAction();

}

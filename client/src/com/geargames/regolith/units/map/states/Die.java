package com.geargames.regolith.units.map.states;

import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 04.04.13
 */
public class Die extends CyclicWarriorState {
    @Override
    public void change(Unit unit, AbstractWarriorState state) {
    }

    @Override
    public byte getAction() {
        return Actions.DIE;
    }
}

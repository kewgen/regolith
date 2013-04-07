package com.geargames.regolith.units.map.states;

import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class Run extends CyclicWarriorState {

    @Override
    public void change(Unit unit, AbstractWarriorState state) {
        switch (state.getAction()) {
            case Actions.STAND:
                state.init(unit);
        }
    }

    @Override
    public byte getAction() {
        return Actions.RUN;
    }
}

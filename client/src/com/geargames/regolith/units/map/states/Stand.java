package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class Stand extends CyclicWarriorState {

    @Override
    public void change( Unit unit, AbstractWarriorState state) {
        switch (state.getAction()) {
            case Actions.SIT_DOWN:
            case Actions.RUN:
            case Actions.STAND_AND_HIT:
            case Actions.STAND_AND_SHOOT:
            case Actions.STAND_AND_DIE:
                break;
            default:
                return;
        }
        state.init(unit);
    }

    @Override
    public byte getAction() {
        return Actions.STAND;
    }
}

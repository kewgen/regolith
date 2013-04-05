package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 04.04.13
 */
public class Sit extends CyclicWarriorState {

    @Override
    public void change( Unit unit, AbstractWarriorState state) {
        switch (state.getAction()){
            case Actions.SIT_AND_SHOOT:
            case Actions.SIT_AND_HIT:
            case Actions.STAND_UP:
            case Actions.SIT_AND_DIE:
                break;
            default:
                return;
        }
        state.init(unit);
    }

    @Override
    public byte getAction() {
        return Actions.SIT;
    }
}

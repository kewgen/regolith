package com.geargames.regolith.units.map.states;

import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 04.04.13
 */
public class Sit extends CyclicWarriorState {

    @Override
    public void change(Unit unit, AbstractWarriorState state) {
        switch (state.getAction()){
            case Actions.SIT_AND_SHOOT:
            case Actions.SIT_AND_HIT:
            case Actions.STAND_UP:
            case Actions.SIT_AND_DIE:
                break;
            case Actions.RUN:
                LinearWarriorState process = unit.getProcess();
                process.setAction(Actions.STAND_UP);
                process.setFinishState(unit.getRun());
                process.init(unit);
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

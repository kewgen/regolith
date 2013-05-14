package com.geargames.regolith.units.map.unit.states;

import com.geargames.common.logging.Debug;
import com.geargames.regolith.units.map.AbstractClientWarriorElement;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.map.unit.UnitLogicComponent;
import com.geargames.regolith.units.map.unit.Actions;

/**
 * User: abarakov
 * Date: 02.05.13
 * <p/>
 * Боец выполняет команды из положения стоя.
 */
public class UnitStandSimpleState extends AbstractUnitSimpleState {
    private byte action;

    public UnitStandSimpleState(byte action) {
        this.action = action;
    }

    @Override
    public byte getAction() {
        return action;
    }

    @Override
    public void change(DynamicCellElement owner, AbstractLogicState newState) {
        UnitLogicComponent logic = ((AbstractClientWarriorElement) owner).getLogic();
        switch (newState.getAction()) {
            case Actions.HUMAN_STAND:
            case Actions.HUMAN_RUN:
            case Actions.HUMAN_STAND_AND_SHOOT:
            case Actions.HUMAN_SIT_DOWN:
//            case Actions.HUMAN_SIT:
//            case Actions.HUMAN_SIT_AND_SHOOT:
//            case Actions.HUMAN_SIT_AND_HIT:
//            case Actions.HUMAN_STAND_UP:
            case Actions.HUMAN_STAND_AND_HIT:
            case Actions.HUMAN_STAND_AND_DIE:
//            case Actions.HUMAN_DIE:
//            case Actions.HUMAN_SIT_AND_DIE:
                break;
            default:
                Debug.critical("Invalid state transition from '" + getAction() + "' to '" + newState.getAction() + "'");
                return;
        }
        logic.pushState(newState);
    }

}

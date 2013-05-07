package com.geargames.regolith.units.map.logic.states;

import com.geargames.common.logging.Debug;
import com.geargames.regolith.units.map.AbstractClientWarriorElement;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.map.states.Actions;

/**
 * User: abarakov
 * Date: 01.05.13
 * <p/>
 * Боец находится в состоянии покоя в положении стоя.
 */
public class UnitStandState extends AbstractLogicState {

    @Override
    public byte getAction() {
        return Actions.HUMAN_STAND;
    }

    @Override
    public void change(DynamicCellElement owner, AbstractLogicState newState) {
        switch (newState.getAction()) {
//            case Actions.HUMAN_STAND:
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
        AbstractClientWarriorElement warrior = (AbstractClientWarriorElement) owner;
        warrior.getLogic().pushState(newState);
    }

    @Override
    public void onStart(DynamicCellElement owner) {
        AbstractClientWarriorElement warrior = (AbstractClientWarriorElement) owner;
        warrior.getGraphic().start(warrior, getAction());
    }

    @Override
    public void onStop(DynamicCellElement owner) {

    }

    @Override
    public boolean onTick(DynamicCellElement owner) {
//        if (Util.getRandom(100) == 0) {
//            //todo: запустить анимацию шевеления бойца. Типа боец зевнул или решил размяться
//        }
        return false;
    }

}

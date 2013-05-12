package com.geargames.regolith.units.map.unit.states;

import com.geargames.common.logging.Debug;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.units.map.AbstractClientWarriorElement;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.map.unit.UnitLogicComponent;
import com.geargames.regolith.units.map.unit.Actions;

/**
 * User: abarakov
 * Date: 01.05.13
 * <p/>
 * Боец садится.
 */
public class UnitSitDownState extends AbstractUnitSimpleState {

    @Override
    public byte getAction() {
        return Actions.HUMAN_SIT_DOWN;
    }

    @Override
    public void change(DynamicCellElement owner, AbstractLogicState newState) {
        UnitLogicComponent logic = ((AbstractClientWarriorElement) owner).getLogic();
        switch (newState.getAction()) {
//            case Actions.HUMAN_STAND:
            case Actions.HUMAN_RUN:
                logic.pushState(logic.getStandUpState());
                break;
//            case Actions.HUMAN_STAND_AND_SHOOT:
//            case Actions.HUMAN_SIT_DOWN:
//            case Actions.HUMAN_SIT:
            case Actions.HUMAN_SIT_AND_SHOOT:
            case Actions.HUMAN_SIT_AND_HIT:
            case Actions.HUMAN_STAND_UP:
//            case Actions.HUMAN_STAND_AND_HIT:
//            case Actions.HUMAN_STAND_AND_DIE:
//            case Actions.HUMAN_DIE:
            case Actions.HUMAN_SIT_AND_DIE:
                break;
            default:
                Debug.critical("Invalid state transition from '" + getAction() + "' to '" + newState.getAction() + "'");
                return;
        }
        logic.pushState(newState);
    }

    @Override
    public void onStart(DynamicCellElement owner) {
        AbstractClientWarriorElement warrior = (AbstractClientWarriorElement) owner;
        warrior.setSitting(true);
        PRegolithPanelManager.getInstance().getBattleScreen().onChangeWarriorSitting(warrior);
        super.onStart(warrior);
    }

}

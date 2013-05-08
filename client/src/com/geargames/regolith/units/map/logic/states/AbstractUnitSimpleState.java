package com.geargames.regolith.units.map.logic.states;

import com.geargames.regolith.units.map.AbstractClientWarriorElement;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.map.states.AbstractUnitScriptGraphicComponent;

/**
 * User: abarakov
 * Date: 01.05.13
 */
public abstract class AbstractUnitSimpleState extends AbstractLogicState {

    @Override
    public void onStart(DynamicCellElement owner) {
        AbstractClientWarriorElement unit = (AbstractClientWarriorElement) owner;
        unit.getGraphic().start(unit, getAction());
    }

    @Override
    public void onStop(DynamicCellElement owner) {

    }

    @Override
    public boolean onTick(DynamicCellElement owner) {
        AbstractUnitScriptGraphicComponent graphic = ((AbstractClientWarriorElement) owner).getGraphic();
        graphic.onTick();
        return graphic.getCyclesCount() > 0;
    }

}

package com.geargames.regolith.units.map;

import com.geargames.regolith.units.map.states.AbstractUnitScriptGraphicComponent;

/**
 * User: abarakov
 * Date: 02.05.13
 */
public abstract class AbstractClientHumanElement extends HumanElement implements DrawableElement, Tickable {

    public abstract HumanLogicComponent getLogic();

    public abstract AbstractUnitScriptGraphicComponent getGraphic();

}

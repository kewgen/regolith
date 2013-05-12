package com.geargames.regolith.units.map.unit;

import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.map.AbstractClientWarriorElement;
import com.geargames.regolith.units.map.GraphicComponent;

/**
 * User: abarakov
 * Date: 02.05.13
 */
public abstract class AbstractUnitScriptGraphicComponent extends GraphicComponent {

    public abstract void start(AbstractClientWarriorElement warrior, byte action);

    public abstract void stop();

    public abstract short getCyclesCount();

    public abstract PUnit getCurrent();

}

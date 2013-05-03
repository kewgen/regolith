package com.geargames.regolith;

import com.geargames.common.Graphics;
import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.map.AbstractClientHumanElement;
import com.geargames.regolith.units.map.GraphicComponent;
import com.geargames.regolith.units.map.states.AbstractUnitScriptGraphicComponent;

/**
 * User: abarakov
 * Date: 02.05.13
 */
public class TestUnitScriptGraphicComponent extends AbstractUnitScriptGraphicComponent {

    @Override
    public void start(AbstractClientHumanElement unit, byte action) {

    }

    @Override
    public void stop() {

    }

    @Override
    public short getCyclesCount() {
        return 1; // типа анимация уже прошла один цикл, в этом случае, логика сразу определит что можно переходить к следующему стейту
    }

    @Override
    public PUnit getCurrent() {
        return null;
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {

    }

    @Override
    public void onTick() {

    }

}

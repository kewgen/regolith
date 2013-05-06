package com.geargames.regolith;

import com.geargames.common.Graphics;
import com.geargames.regolith.units.map.*;

/**
 * User: abarakov
 * Date: 02.05.13
 */
public class TestClientHumanElement extends AbstractClientHumanElement {
    private HumanLogicComponent logic;
    private TestUnitScriptGraphicComponent graphic;

    public TestClientHumanElement() {
        logic = new HumanLogicComponent(this);
        graphic = new TestUnitScriptGraphicComponent();
        logic.initiate();
    }

    @Override
    public HumanLogicComponent getLogic() {
        return logic;
    }

    @Override
    public TestUnitScriptGraphicComponent getGraphic() {
        return graphic;
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {

    }

    @Override
    public void onTick() {
        logic.onTick();
    }

}

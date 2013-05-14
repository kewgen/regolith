package com.geargames.regolith;

import com.geargames.common.Graphics;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.map.unit.UnitLogicComponent;

/**
 * User: abarakov
 * Date: 02.05.13
 */
public class TestClientWarriorElement extends AbstractClientWarriorElement {
    private UnitLogicComponent logic;
    private TestUnitScriptGraphicComponent graphic;

    public TestClientWarriorElement() {
        logic = new UnitLogicComponent(this);
        graphic = new TestUnitScriptGraphicComponent();
        logic.initiate();
    }

    @Override
    public UnitLogicComponent getLogic() {
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

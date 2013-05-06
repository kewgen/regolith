package com.geargames.regolith.units.map;

import com.geargames.common.Graphics;
import com.geargames.regolith.units.map.states.UnitScriptGraphicComponent;

/**
 * User: abarakov
 * Date: 30.04.13
 */
public class ClientHumanElement extends AbstractClientHumanElement {
    private HumanLogicComponent logic;
    private UnitScriptGraphicComponent graphic;

    public ClientHumanElement() {
        logic = new HumanLogicComponent(this);
        graphic = new UnitScriptGraphicComponent();
        logic.initiate();
    }

    @Override
    public HumanLogicComponent getLogic() {
        return logic;
    }

    @Override
    public UnitScriptGraphicComponent getGraphic() {
        return graphic;
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        graphic.draw(graphics, x, y);
    }

    @Override
    public void onTick() {
        graphic.onTick();
        logic.onTick();
    }

}

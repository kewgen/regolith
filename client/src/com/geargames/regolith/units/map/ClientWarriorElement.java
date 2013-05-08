package com.geargames.regolith.units.map;

import com.geargames.common.Graphics;
import com.geargames.regolith.units.map.states.UnitScriptGraphicComponent;

/**
 * User: abarakov
 * Date: 30.04.13
 */
public class ClientWarriorElement extends AbstractClientWarriorElement {
    private HumanLogicComponent logic;
    private UnitScriptGraphicComponent graphic;

    public ClientWarriorElement() {
        logic = new HumanLogicComponent(this);
        graphic = new UnitScriptGraphicComponent();
    }

    @Override
    public HumanLogicComponent getLogic() {
        return logic;
    }

    @Override
    public UnitScriptGraphicComponent getGraphic() {
        return graphic;
    }

    public void initiate() {
        logic.initiate();
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

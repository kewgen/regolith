package com.geargames.regolith.units.map;

import com.geargames.common.Graphics;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.map.unit.UnitScriptGraphicComponent;
import com.geargames.regolith.units.map.unit.UnitLogicComponent;

/**
 * User: abarakov
 * Date: 30.04.13
 */
public class ClientWarriorElement extends AbstractClientWarriorElement {
    private UnitLogicComponent logic;
    private UnitScriptGraphicComponent graphic;

    public ClientWarriorElement() {
        logic = new UnitLogicComponent(this);
        graphic = new UnitScriptGraphicComponent();
    }

    @Override
    public UnitLogicComponent getLogic() {
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
        //todo: getMapX() и getMapY() переименовать в getMapOffsetX() и getMapOffsetY() соответственно и должны они означать смещение бойца относительно центра ячейки которую он занимает
//      graphic.draw(graphics, x + getMapOffsetX(), y + getMapOffsetY());

        BattleScreen battleScreen = PRegolithPanelManager.getInstance().getBattleScreen();
        graphic.draw(graphics, getMapX() - battleScreen.getMapX(), getMapY() - battleScreen.getMapY());
    }

    @Override
    public void onTick() {
        graphic.onTick();
        logic.onTick();
    }

}

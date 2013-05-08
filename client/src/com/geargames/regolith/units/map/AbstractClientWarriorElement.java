package com.geargames.regolith.units.map;

import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.states.AbstractUnitScriptGraphicComponent;

/**
 * User: abarakov
 * Date: 02.05.13
 */
public abstract class AbstractClientWarriorElement extends Warrior implements DrawableElement, Tickable {
    private short mapX;
    private short mapY;

    public AbstractClientWarriorElement() {
        mapX = -32768;
        mapY = -32768;
    }

    /**
     * Получить координаты бойца на карте по оси X.
     *
     * @return
     */
    public short getMapX() {
        return mapX;
    }

    public void setMapX(short x) {
        this.mapX = x;
    }

    /**
     * Получить координаты бойца на карте по оси Y.
     *
     * @return
     */
    public short getMapY() {
        return mapY;
    }

    public void setMapY(short y) {
        this.mapY = y;
    }

    public abstract HumanLogicComponent getLogic();

    public abstract AbstractUnitScriptGraphicComponent getGraphic();

}

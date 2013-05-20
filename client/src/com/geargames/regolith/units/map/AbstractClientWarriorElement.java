package com.geargames.regolith.units.map;

import com.geargames.common.logging.Debug;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.unit.AbstractUnitScriptGraphicComponent;
import com.geargames.regolith.units.map.unit.UnitLogicComponent;

/**
 * User: abarakov
 * Date: 02.05.13
 */
public abstract class AbstractClientWarriorElement extends Warrior implements DrawableElement, Tickable {
    private byte membershipType;
    private short mapX;
    private short mapY;

    public AbstractClientWarriorElement() {
        membershipType = -1;
        mapX = -32768;
        mapY = -32768;
    }

    public byte getMembershipType() {
        if (Debug.IS_DEBUG && (membershipType < WarriorMembershipType.ENEMY || membershipType > WarriorMembershipType.WARRIOR)) {
            throw new RuntimeException("Invalid value of MembershipType (membershipType = " + membershipType + ")");
        }
        return membershipType;
    }

    //todo-asap: Везде ли правильно проставляется принадлежность бойца?
    public void setMembershipType(byte value) {
        membershipType = value;
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

    public abstract UnitLogicComponent getLogic();

    public abstract AbstractUnitScriptGraphicComponent getGraphic();

}

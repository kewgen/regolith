package com.geargames.regolith.units.map;

import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: abarakov
 * Date: 13.05.13
 */
public abstract class Door extends DynamicCellElement {
    private boolean opened;

    public Door() {
        opened = false;
    }

    /**
     * Вернет true, если дверь открыта.
     */
    public boolean getOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    /**
     * Вернет true, если это препятствие в полроста и false, если препятствие выше роста бойца.
     */
    @Override
    public boolean isHalfLong() {
        return false;
    }

    public abstract void setFrameId(int unitFrameId);

    /**
     * Вернет true, если препятствие не является помехой для обзора.
     */
    @Override
    public boolean isAbleToLookThrough() {
        return opened;
    }

    /**
     * Вернет true, если препятствие не является помехой для прохождения через него.
     *
     * @return
     */
    @Override
    public boolean isAbleToWalkThrough() {
        return opened;
    }

    @Override
    public boolean isBarrier() {
        return !opened;
    }

    /**
     * Задать для категории оружия возможность стрелять через данное препятствие.
     *
     * @param category
     * @param able
     */
    public abstract void setAbleToShootThrough(WeaponCategory category, boolean able);

    @Override
    public short getElementType() {
        return CellElementTypes.DOOR;
    }

    @Override
    public byte getLayer() {
        return CellElementLayers.DYNAMIC;
    }

}

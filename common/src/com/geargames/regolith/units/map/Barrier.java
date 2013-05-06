package com.geargames.regolith.units.map;

import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 18.02.12
 * Time: 15:00
 */
public abstract class Barrier extends CellElement {
    private boolean ableToLookThrough;
    private boolean ableToWalkThrough;
    private boolean halfLong;

    /**
     * Вернет true, если это препятствие в полроста и false, если препятствие выше роста бойца.
     *
     * @return
     */
    @Override
    public boolean isHalfLong() {
        return halfLong;
    }

    public void setHalfLong(boolean halfLong) {
        this.halfLong = halfLong;
    }

    public abstract void setFrameId(int unitFrameId);

    /**
     * Вернет true, если препятствие не является помехой для обзора.
     *
     * @return
     */
    @Override
    public boolean isAbleToLookThrough() {
        return ableToLookThrough;
    }

    public void setAbleToLookThrough(boolean ableToLookThrough) {
        this.ableToLookThrough = ableToLookThrough;
    }

    /**
     * Вернет true, если препятствие не является помехой для прохождения через него.
     *
     * @return
     */
    @Override
    public boolean isAbleToWalkThrough() {
        return ableToWalkThrough;
    }

    public void setAbleToWalkThrough(boolean ableToWalkThrough) {
        this.ableToWalkThrough = ableToWalkThrough;
    }

    @Override
    public boolean isBarrier() {
        return true;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.BARRIER;
    }

    /**
     * Задать для категории оружия возможность стрелять через данное препятствие.
     *
     * @param category
     * @param able
     */
    public abstract void setAbleToShootThrough(WeaponCategory category, boolean able);

    @Override
    public byte getLayer() {
        return CellElementLayers.DYNAMIC;
    }

}

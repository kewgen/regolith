package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.CellElementLayers;
import com.geargames.regolith.units.CellElementTypes;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 18.02.12
 * Time: 15:00
 */
public abstract class Barrier extends CellElement {
    private int frameId;
    private boolean ableToLookThrough;
    private boolean ableToWalkThrough;
    private boolean halfLong;

    /**
     * ??? Вернет true, если это препятствие в полроста и false, если препятствие выше роста бойца.
     * @return
     */
    public boolean isHalfLong() {
        return halfLong;
    }

    public void setHalfLong(boolean halfLong) {
        this.halfLong = halfLong;
    }

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int unitFrameId) {
        this.frameId = unitFrameId;
    }

    /**
     * ??? Вернет true, если препятствие не является помехой для обзора.
     * @return
     */
    public boolean isAbleToLookThrough() {
        return ableToLookThrough;
    }

    public void setAbleToLookThrough(boolean ableToLookThrough) {
        this.ableToLookThrough = ableToLookThrough;
    }

    /**
     * ??? Вернет true, если препятствие не является помехой для прохождения через него.
     * @return
     */
    public boolean isAbleToWalkThrough() {
        return ableToWalkThrough;
    }

    public void setAbleToWalkThrough(boolean ableToWalkThrough) {
        this.ableToWalkThrough = ableToWalkThrough;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.BARRIER;
    }

    /**
     * Задать для категории оружия возможность стрелять через данное препятствие.
     * @param category
     * @param able
     */
    public abstract void setAbleToShootThrough(WeaponCategory category, boolean able);

    @Override
    public byte getLayer() {
        return CellElementLayers.DYNAMIC;
    }

}

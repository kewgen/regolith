package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.ElementTypes;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 18.02.12
 * Time: 15:00
 */
public abstract class Border extends Element {
    private int frameId;
    private boolean ableToLookThrough;
    private boolean ableToWalkThrough;
    private boolean halfLong;

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

    public boolean isAbleToLookThrough() {
        return ableToLookThrough;
    }

    public void setAbleToLookThrough(boolean ableToLookThrough) {
        this.ableToLookThrough = ableToLookThrough;
    }

    public boolean isAbleToWalkThrough() {
        return ableToWalkThrough;
    }

    public void setAbleToWalkThrough(boolean ableToWalkThrough) {
        this.ableToWalkThrough = ableToWalkThrough;
    }

    @Override
    public short getElementType() {
        return ElementTypes.BORDER;
    }

    public abstract void setAbleToShootThrough(WeaponCategory category, boolean able);
}

package com.geargames.regolith.units.map;

import com.geargames.regolith.units.tackle.WeaponCategory;

import java.util.Map;

/**
 * User: abarakov
 * Date: 13.05.13
 */
public class ServerDoor extends Door {
    private int frameId;
    private Map<WeaponCategory, Boolean> shootThrough;

    @Override
    public int getFrameId() {
        return frameId;
    }

    @Override
    public void setFrameId(int unitFrameId) {
        this.frameId = unitFrameId;
    }

    public Map<WeaponCategory, Boolean> getShootThrough() {
        return shootThrough;
    }

    public void setShootThrough(Map<WeaponCategory, Boolean> shootThrough) {
        this.shootThrough = shootThrough;
    }

    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return shootThrough.get(weaponCategory);
    }

    public void setAbleToShootThrough(WeaponCategory category, boolean able) {
        shootThrough.put(category, able);
    }

}

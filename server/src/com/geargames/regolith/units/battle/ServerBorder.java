package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.tackle.WeaponCategory;

import java.util.Map;

/**
 * User: mkutuzov
 * Date: 28.03.12
 */
public class ServerBorder extends Border {
    private Map<WeaponCategory, Boolean> shootThrough;

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

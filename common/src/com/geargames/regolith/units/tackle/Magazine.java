package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.Element;

/**
 * Особая сущность - куча зарядов, существует только когда бойцу надо выложить(забрать) заряды из сумки
 * на поле боя или в коробку.
 * User: mkutuzov
 * Date: 29.03.12
 */
public class Magazine extends Element {
    private Projectile projectile;
    private short count;

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    /**
     * Количество зарядов.
     * @return
     */
    public short getCount() {
        return count;
    }

    public void setCount(short count) {
        this.count = count;
    }

    public boolean isAbleToLookThrough() {
        return true;
    }

    public boolean isAbleToWalkThrough() {
        return true;
    }

    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return true;
    }

    public boolean isHalfLong() {
        return true;
    }

    public int getFrameId() {
        return projectile.getFrameId();
    }
}
package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.map.CellElementLayers;
import com.geargames.regolith.units.map.CellElementTypes;

/**
 * Особая сущность - куча зарядов, существует только тогда, когда бойцу надо выложить заряды из сумки
 * на поле боя (в коробку) или забрать их с поля боя в сумку.
 * User: mkutuzov
 * Date: 29.03.12
 */
public class Magazine extends CellElement {
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
     *
     * @return
     */
    public short getCount() {
        return count;
    }

    public void setCount(short count) {
        this.count = count;
    }

    @Override
    public boolean isAbleToLookThrough() {
        return true;
    }

    @Override
    public boolean isAbleToWalkThrough() {
        return true;
    }

    @Override
    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return true;
    }

    @Override
    public boolean isHalfLong() {
        return true;
    }

    @Override
    public int getFrameId() {
        return projectile.getFrameId();
    }

    @Override
    public boolean isBarrier() {
        return false;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.MAGAZINE;
    }

    @Override
    public byte getLayer() {
        return CellElementLayers.TACKLE;
    }

}

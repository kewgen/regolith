package com.geargames.regolith.units.map;

import com.geargames.common.Graphics;
import com.geargames.common.env.Environment;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * Реализация клиентского класса двери. Целиком опирается на то, что weaponCategory.getId() возвращает
 * последовательные номера категорий, которые лежат в области от 0 до 7.
 * User: abarakov
 * Date: 13.05.13
 */
public class ClientDoor extends Door implements DrawableElement {
    private PObject obj;
    private byte shootThrough;

    @Override
    public int getFrameId() {
        return obj.getPID();
    }

    @Override
    public void setFrameId(int unitFrameId) {
        obj = Environment.getRender().getObject(unitFrameId);
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        obj.draw(graphics, x, y);
    }

    public byte getShootThrough() {
        return shootThrough;
    }

    public void setShootThrough(byte shootThrough) {
        this.shootThrough = shootThrough;
    }

    @Override
    public void setAbleToShootThrough(WeaponCategory category, boolean able) {
        if (able) {
            shootThrough |= 1 << category.getId();
        } else {
            shootThrough &= ~(1 << category.getId());
        }
    }

    @Override
    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return ((shootThrough >> weaponCategory.getId()) & 1) != 0;
    }

}

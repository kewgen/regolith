package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * Реализация клиентского класса преграды целиком опирается на то, что weaponCategory.getId() возвращает
 * последовательные номера категорий, которые лежат в области от 0 до 7.
 * User: mkutuzov
 * Date: 28.03.12
 */
public class ClientBarrier extends Barrier {
    private byte shootThrough;

    public byte getShootThrough() {
        return shootThrough;
    }

    public void setShootThrough(byte shootThrough) {
        this.shootThrough = shootThrough;
    }

    public void setAbleToShootThrough(WeaponCategory category, boolean able){
        if(able){
            shootThrough |= 1 << category.getId();
        }else{
            shootThrough &= ~(1 << category.getId());
        }
    }

    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return ((shootThrough >> weaponCategory.getId()) & 1) != 0;
    }

}

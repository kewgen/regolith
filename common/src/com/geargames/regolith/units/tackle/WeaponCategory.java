package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.dictionaries.WeaponTypeCollection;

/**
 * User: mkutuzov
 * Date: 03.02.12
 */
public class WeaponCategory extends Entity {
    private String name;
    private WeaponTypeCollection weaponTypes;
    private int packerId;

    /**
     * Иденитификатор в пакере первого скрипта из списка скриптов, где использована данная разновидность оружия.
     * @return
     */
    public int getPackerId() {
        return packerId;
    }

    public void setPackerId(int packerId) {
        this.packerId = packerId;
    }

    /**
     * Наименование разновидности оружия.
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Типы оружия которые попадают под данную разновидность.
     * @return
     */
    public WeaponTypeCollection getWeaponTypes() {
        return weaponTypes;
    }

    public void setWeaponTypes(WeaponTypeCollection weaponTypes) {
        this.weaponTypes = weaponTypes;
    }
}

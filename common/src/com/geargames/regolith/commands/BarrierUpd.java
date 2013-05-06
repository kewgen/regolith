package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.map.Barrier;
import com.geargames.regolith.units.dictionaries.BarrierCollection;
import com.geargames.regolith.units.dictionaries.WeaponCategoryCollection;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class BarrierUpd extends BaseConfigurationCommand {
    private Barrier barrier;

    public BarrierUpd(Barrier barrier) {
        this.barrier = barrier;
    }

    public void command(BaseConfiguration configuration) {
        BarrierCollection collection = configuration.getBarriers();
        int length = collection.size();
        int id = barrier.getId();
        for (int i = 0; i < length; i++) {
            if (collection.get(i).getId() == id) {
                Barrier local = collection.get(i);
                local.setAbleToLookThrough(barrier.isAbleToLookThrough());
                local.setAbleToWalkThrough(barrier.isAbleToWalkThrough());
                local.setFrameId(barrier.getFrameId());
                local.setHalfLong(barrier.isHalfLong());
                WeaponCategoryCollection weaponCategoryDictionary = configuration.getWeaponCategories();
                int size = weaponCategoryDictionary.size();
                for (int j = 0; j < size; j++) {
                    WeaponCategory category = weaponCategoryDictionary.get(j);
                    local.setAbleToShootThrough(category, barrier.isAbleToShootThrough(category));
                }
                break;
            }
        }
    }

}

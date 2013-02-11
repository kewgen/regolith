package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.battle.Border;
import com.geargames.regolith.units.dictionaries.BorderCollection;
import com.geargames.regolith.units.dictionaries.WeaponCategoryCollection;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class BorderUpd extends BaseConfigurationCommand {
    private Border border;

    public BorderUpd(Border border) {
        this.border = border;
    }

    public void command(BaseConfiguration configuration) {
        BorderCollection collection = configuration.getBorders();
        int length = collection.size();
        int id = border.getId();
        for (int i = 0; i < length; i++) {
            if (collection.get(i).getId() == id) {
                Border local = collection.get(i);
                local.setAbleToLookThrough(border.isAbleToLookThrough());
                local.setAbleToWalkThrough(border.isAbleToWalkThrough());
                local.setFrameId(border.getFrameId());
                local.setHalfLong(border.isHalfLong());
                WeaponCategoryCollection weaponCategoryDictionary = configuration.getWeaponCategories();
                int size = weaponCategoryDictionary.size();
                for(int j = 0; j < size; j++){
                    WeaponCategory category = weaponCategoryDictionary.get(j);
                    local.setAbleToShootThrough(category, border.isAbleToShootThrough(category));
                }
                break;
            }
        }
    }
}

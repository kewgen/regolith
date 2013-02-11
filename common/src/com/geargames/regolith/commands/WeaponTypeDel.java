package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.WeaponCategoryCollection;
import com.geargames.regolith.units.dictionaries.WeaponTypeCollection;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public class WeaponTypeDel extends BaseConfigurationCommand {
    private int id;

    public WeaponTypeDel(int id) {
        this.id = id;
    }

    public void command(BaseConfiguration configuration) {
        WeaponCategoryCollection categories = configuration.getWeaponCategories();
        for (int i = 0; i < categories.size(); i++) {
            WeaponTypeCollection types = categories.get(i).getWeaponTypes();
            for (int j = 0; j < types.size(); j++) {
                if (types.get(j).getId() == id) {
                    types.remove(j);
                    break;
                }
            }
        }
    }
}

package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class WeaponCategoryAdd extends BaseConfigurationCommand {
    private WeaponCategory category;

    public WeaponCategoryAdd(WeaponCategory category) {
        this.category = category;
    }

    public void command(BaseConfiguration configuration) {
        configuration.getWeaponCategories().add(category);
    }
}

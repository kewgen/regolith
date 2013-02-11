package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.WeaponCategoryCollection;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class WeaponCategoryUpd extends BaseConfigurationCommand {
    private WeaponCategory category;

    public WeaponCategoryUpd(WeaponCategory category) {
        this.category = category;
    }

    public void command(BaseConfiguration configuration) {
        WeaponCategoryCollection dictionary = configuration.getWeaponCategories();
        int length = dictionary.size();
        int id = category.getId();
        for(int i = 0; i < length; i++){
            if(dictionary.get(i).getId() == id){
                WeaponCategory local = dictionary.get(i);
                if(local.getId() == id){
                    local.setName(category.getName());
                }
                break;
            }
        }
    }
}

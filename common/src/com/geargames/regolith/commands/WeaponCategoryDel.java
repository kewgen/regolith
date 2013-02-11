package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.WeaponCategoryCollection;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class WeaponCategoryDel extends BaseConfigurationCommand {

    private int id;

    public WeaponCategoryDel(int id) {
        this.id = id;
    }

    public void command(BaseConfiguration configuration) {
        WeaponCategoryCollection dictionary = configuration.getWeaponCategories();
        int length = dictionary.size();
        for(int i = 0; i < length; i++){
            if(dictionary.get(i).getId() == id){
                dictionary.remove(i);
                break;
            }
        }
    }
}

package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.ArmorTypeCollection;

/**
 * User: mkutuzov
 * Date: 27.04.12
 */
public class ArmorTypeDel extends BaseConfigurationCommand {
    private int id;

    public ArmorTypeDel(int id) {
        this.id = id;
    }

    public void command(BaseConfiguration configuration) {
        ArmorTypeCollection collection = configuration.getArmorTypes();
        int size  = collection.size();
        for(int i =0; i < size; i++){
            if(collection.get(i).getId() == id){
                collection.remove(i);
                break;
            }
        }
    }
}

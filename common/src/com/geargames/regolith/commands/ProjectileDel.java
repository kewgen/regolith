package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.ProjectileCollection;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class ProjectileDel extends BaseConfigurationCommand {
    private int id;

    public ProjectileDel(int id) {
        this.id = id;
    }

    public void command(BaseConfiguration configuration) {
        ProjectileCollection dictionary = configuration.getProjectiles();
        int length = dictionary.size();
        for(int i = 0; i < length; i++){
            if(dictionary.get(i).getId() == id){
                dictionary.remove(i);
                break;
            }
        }
    }
}

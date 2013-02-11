package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.BorderCollection;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class BorderDel extends BaseConfigurationCommand {
    private int id;

    public BorderDel(int id) {
        this.id = id;
    }

    public void command(BaseConfiguration configuration) {
        BorderCollection borders = configuration.getBorders();
        int length = borders.size();
        for(int  i = 0; i < length; i++){
            if(borders.get(i).getId() == id){
                borders.remove(i);
                break;
            }
        }
    }
}

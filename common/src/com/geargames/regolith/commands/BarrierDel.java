package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.BarrierCollection;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class BarrierDel extends BaseConfigurationCommand {
    private int id;

    public BarrierDel(int id) {
        this.id = id;
    }

    public void command(BaseConfiguration configuration) {
        BarrierCollection barriers = configuration.getBarriers();
        int length = barriers.size();
        for (int i = 0; i < length; i++) {
            if (barriers.get(i).getId() == id) {
                barriers.remove(i);
                break;
            }
        }
    }

}

package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.map.Barrier;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class BarrierAdd extends BaseConfigurationCommand {

    private Barrier barrier;

    public BarrierAdd(Barrier barrier) {
        this.barrier = barrier;
    }

    public void command(BaseConfiguration configuration) {
        configuration.getBarriers().add(barrier);
    }

}

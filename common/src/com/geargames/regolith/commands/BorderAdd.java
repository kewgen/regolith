package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.battle.Border;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class BorderAdd extends BaseConfigurationCommand{

    private Border border;

    public BorderAdd(Border border) {
        this.border = border;
    }

    public void command(BaseConfiguration configuration) {
        configuration.getBorders().add(border);
    }
}

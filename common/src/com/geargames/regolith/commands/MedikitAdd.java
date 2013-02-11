package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.tackle.Medikit;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class MedikitAdd extends BaseConfigurationCommand {
    private Medikit medikit;

    public MedikitAdd(Medikit medikit) {
        this.medikit = medikit;
    }

    public void command(BaseConfiguration configuration) {
        configuration.getMedikits().add(medikit);
    }
}

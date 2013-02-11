package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.tackle.ArmorType;

/**
 * User: mkutuzov
 * Date: 27.04.12
 */
public class ArmorTypeAdd extends BaseConfigurationCommand {
    private ArmorType armorType;

    public ArmorTypeAdd(ArmorType armorType) {
        this.armorType = armorType;
    }

    public void command(BaseConfiguration configuration) {
        configuration.getArmorTypes().add(armorType);
    }
}

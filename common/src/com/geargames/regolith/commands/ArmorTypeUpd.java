package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.ArmorTypeCollection;
import com.geargames.regolith.units.tackle.ArmorType;

/**
 * User: mkutuzov
 * Date: 27.04.12
 */
public class ArmorTypeUpd extends BaseConfigurationCommand {
    private ArmorType armorType;

    public ArmorTypeUpd(ArmorType armorType) {
        this.armorType = armorType;
    }

    public void command(BaseConfiguration configuration) {
        ArmorTypeCollection collection = configuration.getArmorTypes();
        int size = collection.size();
        int id = armorType.getId();
        for (int i = 0; i < size; i++) {
            ArmorType local = collection.get(i);
            if (local.getId() == id) {
                local.setArmor(armorType.getArmor());
                local.setBaseFirmness(armorType.getBaseFirmness());
                local.setBodyParticle(armorType.getBodyParticle());
                local.setCraftinessBonus(armorType.getCraftinessBonus());
                local.setMarksmanshipBonus(armorType.getMarksmanshipBonus());
                local.setRegenerationBonus(armorType.getRegenerationBonus());
                local.setSpeedBonus(armorType.getSpeedBonus());
                local.setStrengthBonus(armorType.getStrengthBonus());
                local.setVisibilityBonus(armorType.getVisibilityBonus());
                local.setFrameId(armorType.getFrameId());
                local.setName(armorType.getName());
                local.setWeight(armorType.getWeight());
                break;
            }
        }
    }
}

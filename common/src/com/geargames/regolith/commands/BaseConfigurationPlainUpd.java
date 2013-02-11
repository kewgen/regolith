package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;

/**
 * User: mkutuzov
 * Date: 24.04.12
 */
public class BaseConfigurationPlainUpd extends BaseConfigurationCommand {

    private BaseConfiguration newConfiguration;

    public BaseConfigurationPlainUpd(BaseConfiguration newConfiguration) {
        this.newConfiguration = newConfiguration;
    }

    public void command(BaseConfiguration configuration) {
        configuration.setRevision((short)(configuration.getRevision()+1));
        configuration.setPocketsAmount(newConfiguration.getPocketsAmount());
        configuration.setMarksmanshipStep(newConfiguration.getMaxWorkShopLevel());
        configuration.setMaxWorkShopProbability(newConfiguration.getMaxWorkShopProbability());
        configuration.setMinWorkShopProbability(newConfiguration.getMinWorkShopProbability());
        configuration.setVitalityStep(newConfiguration.getVitalityStep());
        configuration.setStrengthStep(newConfiguration.getStrengthStep());
        configuration.setSpeedStep(newConfiguration.getSpeedStep());
        configuration.setMarksmanshipStep(newConfiguration.getMarksmanshipStep());
        configuration.setCraftinessStep(newConfiguration.getCraftinessStep());
        configuration.setBaseActionScore(newConfiguration.getBaseActionScore());
        configuration.setBaseHealth(newConfiguration.getBaseHealth());
        configuration.setBaseStrength(newConfiguration.getBaseStrength());
        configuration.setBaseCraftiness(newConfiguration.getBaseCraftiness());
        configuration.setBaseMarksmanship(newConfiguration.getBaseMarksmanship());
    }

}

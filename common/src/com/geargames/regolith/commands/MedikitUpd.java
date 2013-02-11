package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.MedikitCollection;
import com.geargames.regolith.units.tackle.Medikit;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class MedikitUpd extends BaseConfigurationCommand {
    private Medikit medikit;

    public MedikitUpd(Medikit medikit) {
        this.medikit = medikit;
    }

    public void command(BaseConfiguration configuration) {
        MedikitCollection dictionary = configuration.getMedikits();
        int length = dictionary.size();
        int id  = medikit.getId();
        for(int i=0; i < length; i++){
            if(dictionary.get(i).getId() == id){
                Medikit local = dictionary.get(i);
                local.setCategory(medikit.getCategory());
                local.setFrameId(medikit.getFrameId());
                local.setWeight(medikit.getWeight());
                local.setActionScores(medikit.getActionScores());
                local.setMinSkill(medikit.getMinSkill());
                local.setValue(medikit.getValue());
                local.setName(medikit.getName());
                break;
            }
        }
    }
}

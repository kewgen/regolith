package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.dictionaries.SkillCollection;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class SkillUpd extends BaseConfigurationCommand {
    private Skill skill;

    public SkillUpd(Skill skill) {
        this.skill = skill;
    }

    public void command(BaseConfiguration configuration) {
        SkillCollection dictionary = configuration.getSkills();
        int length = dictionary.size();
        int id = skill.getId();
        for(int i = 0; i < length; i++){
            if(dictionary.get(i).getId() == id){
                Skill local = dictionary.get(i);
                local.setAction(skill.getAction());
                local.setExperience(skill.getExperience());
                break;
            }
        }

    }
}

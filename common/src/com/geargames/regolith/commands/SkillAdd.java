package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.Skill;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class SkillAdd extends BaseConfigurationCommand {
    private Skill skill;

    public SkillAdd(Skill skill) {
        this.skill = skill;
    }

    public void command(BaseConfiguration configuration) {
        configuration.getSkills().add(skill);
    }
}

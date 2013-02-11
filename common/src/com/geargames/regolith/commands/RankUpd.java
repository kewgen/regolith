package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.Rank;
import com.geargames.regolith.units.dictionaries.RankCollection;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class RankUpd extends BaseConfigurationCommand {
    private Rank rank;

    public RankUpd(Rank rank) {
        this.rank = rank;
    }

    public void command(BaseConfiguration configuration) {
        RankCollection dictionary = configuration.getRanks();
        int length = dictionary.size();
        int id = rank.getId();
        for(int i = 0; i < length; i++){
            if(dictionary.get(i).getId() == id){
                Rank local = dictionary.get(i);
                local.setExperience(rank.getExperience());
                local.setName(rank.getName());
                break;
            }
        }
    }
}

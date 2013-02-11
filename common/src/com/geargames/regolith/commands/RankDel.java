package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.Rank;
import com.geargames.regolith.units.dictionaries.RankCollection;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class RankDel extends BaseConfigurationCommand {
    private Rank rank;

    public RankDel(Rank rank) {
        this.rank = rank;
    }

    public void command(BaseConfiguration configuration) {
        RankCollection dictionary = configuration.getRanks();
        int length = dictionary.size();
        int id = rank.getId();
        for( int i = 0; i < length; i++){

        }
    }
}

package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.Rank;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class RankAdd extends BaseConfigurationCommand {
    private Rank rank;

    public RankAdd(Rank rank) {
        this.rank = rank;
    }

    public void command(BaseConfiguration configuration) {
        configuration.getRanks().add(rank);
    }
}

package com.geargames.regolith;

import java.io.Serializable;

public class RegolithConfiguration implements Serializable {
    private BattleConfiguration battleConfiguration;
    private BaseConfiguration baseConfiguration;

    public BattleConfiguration getBattleConfiguration() {
        return battleConfiguration;
    }

    public void setBattleConfiguration(BattleConfiguration battleConfiguration) {
        this.battleConfiguration = battleConfiguration;
    }

    public BaseConfiguration getBaseConfiguration() {
        return baseConfiguration;
    }

    public void setBaseConfiguration(BaseConfiguration baseConfiguration) {
        this.baseConfiguration = baseConfiguration;
    }
}

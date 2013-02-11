package com.geargames.regolith.units;

import com.geargames.regolith.units.dictionaries.StateTackleCollection;

/**
 * User: mkutuzov
 * Date: 06.02.12
 */
public class Bag extends Entity {
    private StateTackleCollection tackles;
    private short weight;

    /**
     *
     * @return
     */
    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public StateTackleCollection getTackles() {
        return tackles;
    }

    public void setTackles(StateTackleCollection tackles) {
        this.tackles = tackles;
    }
}

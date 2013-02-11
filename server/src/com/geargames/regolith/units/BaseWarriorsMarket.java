package com.geargames.regolith.units;

import com.geargames.regolith.units.battle.Warrior;

import java.util.Set;

/**
 * User: mkutuzov
 * Date: 10.07.12
 */
public class BaseWarriorsMarket {
    private int revision;
    private Set<Warrior> warriors;

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public Set<Warrior> getWarriors() {
        return warriors;
    }

    public void setWarriors(Set<Warrior> warriors) {
        this.warriors = warriors;
    }
}

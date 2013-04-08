package com.geargames.regolith.units.base;

import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 17.03.12
 */
public class ShootingRange implements Serializable{
    private byte level;
    private WarriorCollection warriors;

    public WarriorCollection getWarriors() {
        return warriors;
    }

    public void setWarriors(WarriorCollection warriors) {
        this.warriors = warriors;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }
}

package com.geargames.regolith.units.base;

import com.geargames.regolith.units.Entity;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 17.03.12
 */
public class ClearingShop implements Serializable {
    private byte level;

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }
}

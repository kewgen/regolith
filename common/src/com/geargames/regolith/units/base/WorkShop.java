package com.geargames.regolith.units.base;

import com.geargames.regolith.units.Entity;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 26.02.12
 */
public class WorkShop implements Serializable{
    private byte level;

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }
}

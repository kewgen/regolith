package com.geargames.regolith.units.base;

import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.StateTackle;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 17.03.12
 */
public class StoreHouse implements Serializable{
    private byte level;
    private Bag bag;
    private AmmunitionBag ammunitionBag;

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public AmmunitionBag getAmmunitionBag() {
        return ammunitionBag;
    }

    public void setAmmunitionBag(AmmunitionBag ammunitionBag) {
        this.ammunitionBag = ammunitionBag;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }
}

package com.geargames.regolith.units;

import com.geargames.regolith.units.tackle.Ammunition;

/**
 * @author Mikhail_Kutuzov
 *
 */
public class AmmunitionPacket extends Entity {
    private Ammunition ammunition;
    private short count;

    public Ammunition getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(Ammunition ammunition) {
        this.ammunition = ammunition;
    }

    public short getCount() {
        return count;
    }

    public void setCount(short count) {
        this.count = count;
    }
}

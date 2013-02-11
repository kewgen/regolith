package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.tackle.AmmunitionCategory;

/**
 * User: mikhail v. kutuzov
 *
 */
public abstract class AmmunitionPacketCollection extends EntityCollection {
    public abstract AmmunitionPacket get(int index);
    public abstract void add(AmmunitionPacket packet);
    public abstract void insert(AmmunitionPacket packet, int index);
    public abstract void set(AmmunitionPacket packet, int  index);
}

package com.geargames.regolith;

import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.AmmunitionPacket;
import com.geargames.regolith.units.dictionaries.ServerAmmunitionPacketCollection;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: mikhail v. kutuzov
 * Date: 11.12.12
 * Time: 12:31
 */
public class ServerHelper {
    public static AmmunitionBag createAmmunitionBag(BaseConfiguration baseConfiguration) {
        AmmunitionBag bag = new AmmunitionBag();
        ServerAmmunitionPacketCollection packets = new ServerAmmunitionPacketCollection();
        packets.setPackets(new ArrayList<AmmunitionPacket>(baseConfiguration.getPocketsAmount()));
        bag.setPackets(packets);
        packets = new ServerAmmunitionPacketCollection();
        packets.setPackets(new LinkedList<AmmunitionPacket>());
        bag.setReserve(packets);
        bag.setWeight(0);
        return bag;
    }
}

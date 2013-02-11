package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.AmmunitionPacket;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Date: 11.12.12
 * Time: 10:53
 */
public class ClientAmmunitionPacketCollection extends AmmunitionPacketCollection {
    private Vector packets;

    public Vector getPackets() {
        return packets;
    }

    public void setPackets(Vector packets) {
        this.packets = packets;
    }

    public AmmunitionPacket get(int index) {
        return (AmmunitionPacket)packets.get(index);
    }

    public void add(AmmunitionPacket packet) {
        packets.addElement(packet);
    }

    public void insert(AmmunitionPacket packet, int index) {
        packets.insertElementAt(packet, index);
    }

    public void set(AmmunitionPacket packet, int index) {
        packets.setElementAt(packet, index);
    }

    public void remove(int index) {
        packets.remove(index);
    }

    public int size() {
        return packets.size();
    }
}

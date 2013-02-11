package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.AmmunitionPacket;

import java.util.List;

/**
 * User: mikhail v. kutuzov
 *
 **/
public class ServerAmmunitionPacketCollection extends AmmunitionPacketCollection {
    private List<AmmunitionPacket> packets;

    public List<AmmunitionPacket> getPackets() {
        return packets;
    }

    public void setPackets(List<AmmunitionPacket> packets) {
        this.packets = packets;
    }

    public AmmunitionPacket get(int index) {
        return packets.get(index);
    }

    public void add(AmmunitionPacket packet) {
        packets.add(packet);
    }

    public void insert(AmmunitionPacket packet, int index) {
        packets.add(index, packet);
    }

    public void set(AmmunitionPacket packet, int index) {
        packets.set(index, packet);
    }

    public void remove(int index) {
        packets.remove(index);
    }

    public int size() {
        return packets.size();
    }
}

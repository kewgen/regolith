package com.geargames.regolith.units;

import com.geargames.regolith.units.dictionaries.AmmunitionPacketCollection;

/**
 * @author Mikhail_Kutuzov
 *         created: 01.06.12  13:52
 */
public class AmmunitionBag extends Entity {
    private AmmunitionPacketCollection reserve;
    private AmmunitionPacketCollection packets;
    private int weight;

    /**
     * Вернуть коллекцию, которая используется хранения объектов-ячеек аммуниции.
     * @return
     */
    public AmmunitionPacketCollection getReserve() {
        return reserve;
    }

    public void setReserve(AmmunitionPacketCollection reserve) {
        this.reserve = reserve;
    }

    /**
     * Вернуть набор ячеек аммуниции.
     * @return
     */
    public AmmunitionPacketCollection getPackets() {
        return packets;
    }

    public void setPackets(AmmunitionPacketCollection packets) {
        this.packets = packets;
    }

    public short getSize() {
        return (short)packets.size();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

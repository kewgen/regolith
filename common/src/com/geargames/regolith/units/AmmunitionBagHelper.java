package com.geargames.regolith.units;

import com.geargames.regolith.units.dictionaries.AmmunitionPacketCollection;
import com.geargames.regolith.units.tackle.Ammunition;

/**
 * User: mkutuzov
 * Date: 10.02.12
 *
 * Здесь мы работаем со структурой данных сумка бойца.
 * Сумка бойца разбита на карманы(AmmunitionPacket). В каждом кармане лежит единственный тип снаряжения (Tackle)
 * в количестве count.
 */
public class AmmunitionBagHelper {

    /**
     * Удалить ячейку с аммуницией по номеру.
     *
     * @param bag
     * @param index
     */
    public static void removePacket(AmmunitionBag bag, int index) {
        AmmunitionPacketCollection packets = bag.getPackets();
        AmmunitionPacket packet = packets.get(index);
        int packetWeight = packet.getCount() * packet.getAmmunition().getWeight();
        packets.remove(index);
        bag.getReserve().add(packet);
        bag.setWeight(bag.getWeight() - packetWeight);
    }

    /**
     * Кладём снаряжение ammunition в количестве amount внутрь сумки bag
     *
     * @param bag
     * @param ammunition
     * @param amount
     * @return количество убранного в сумку снаряжения(может быть меньше amount).
     */
    public static int putIn(AmmunitionBag bag, Ammunition ammunition, int amount) {
        int index = getPacketIndex(bag, ammunition);

        if (index != -1) {
            AmmunitionPacket packet = bag.getPackets().get(index);
            packet.setCount((short) (packet.getCount() + amount));
        } else {
            AmmunitionPacketCollection reserve = bag.getReserve();
            AmmunitionPacket packet;
            if (reserve.size() > 0) {
                packet = reserve.get(0);
                reserve.remove(0);
            } else {
                packet = new AmmunitionPacket();
            }
            packet.setAmmunition(ammunition);
            packet.setCount((short)amount);
            bag.getPackets().add(packet);
        }
        bag.setWeight(bag.getWeight() + ammunition.getWeight() * amount);
        return amount;
    }

    private static int getPacketIndex(AmmunitionBag bag, Ammunition ammunition) {
        AmmunitionPacketCollection packets = bag.getPackets();
        int length = bag.getSize();
        for (int i = 0; i < length; i++) {
            if (packets.get(i).getAmmunition().getId() == ammunition.getId()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Выложить из сумки bag расходник ammunition в количестве amount.
     * @param bag
     * @param ammunition
     * @param amount
     * @return действительно вынутое количество расходника
     */
    public static int putOut(AmmunitionBag bag, Ammunition ammunition, int amount) {
        int index = getPacketIndex(bag, ammunition);
        if (index != -1) {
            AmmunitionPacketCollection packets = bag.getPackets();
            AmmunitionPacketCollection reserve = bag.getReserve();
            AmmunitionPacket packet = packets.get(index);
            int count = packet.getCount();
            if (count > amount) {
                packet.setCount((short) (packet.getCount() - amount));
                bag.setWeight(bag.getWeight() - ammunition.getWeight() * amount);
                return amount;
            } else {
                packets.remove(index);
                reserve.add(packet);
                bag.setWeight(bag.getWeight() - ammunition.getWeight() * count);
                return count;
            }
        } else {
            return 0;
        }
    }


    /**
     * Содержит ли сумка bag подобную ammunition разновидность аммуниции.
     *
     * @param bag
     * @param ammunition
     * @return
     */
    public static boolean contains(AmmunitionBag bag, Ammunition ammunition) {
        AmmunitionPacketCollection packets = bag.getPackets();
        int length = bag.getSize();
        for (int i = 0; i < length; i++) {
            if (packets.get(i).getAmmunition().getId() == ammunition.getId()) {
                return true;
            }
        }
        return false;
    }

}

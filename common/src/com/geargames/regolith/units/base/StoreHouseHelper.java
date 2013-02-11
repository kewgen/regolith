package com.geargames.regolith.units.base;

import com.geargames.regolith.units.*;
import com.geargames.regolith.units.dictionaries.StateTackleCollection;
import com.geargames.regolith.units.tackle.Ammunition;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 * Склад устроен таким образом, что сначала на нём лежат полки с кладью, а за тем с аммуницией.
 *
 */
public class StoreHouseHelper {

    /**
     * Добавить броню или оружие на склад.
     *
     * @param store склад на который убираем кладь
     * @param tackle кладь
     * @return false если не удалось положить
     */
    public static boolean add(StoreHouse store, StateTackle tackle) {
        if (getWeight(store) + tackle.getWeight() <= getMaxWeight(store)) {
            BagHelper.putIntoBag(store.getBag(), tackle);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Добавить аммуницию ammunition на склад store в количестве amount.
     *
     * @param store
     * @param ammunition
     * @param amount
     * @return сколько аммуниции положили на склад.
     */
    public static int add(StoreHouse store, Ammunition ammunition, int amount) {
        int weight = ammunition.getWeight() * amount;
        int maxWeight = getMaxWeight(store);
        int currentWeight = getWeight(store);
        if (maxWeight >= currentWeight + weight) {
            AmmunitionBagHelper.putIn(store.getAmmunitionBag(), ammunition, amount);
            return amount;
        } else {
            int difference = maxWeight - currentWeight;
            amount = difference/ammunition.getWeight();
            AmmunitionBagHelper.putIn(store.getAmmunitionBag(), ammunition, amount);
            return amount;
        }
    }

    /**
     * Вернуть вместимость склада(в граммах).
     *
     * @param storeHouse
     * @return
     */
    public static int getMaxWeight(StoreHouse storeHouse) {
        return 10000 + storeHouse.getLevel() * 10000;
    }

    /**
     * Вернуть броню или оружие с полки с индексом index.
     *
     * @param storeHouse
     * @param index индекс полки на складе
     * @return
     */
    public static StateTackle getStateTackle(StoreHouse storeHouse, int index) {
        Bag bag = storeHouse.getBag();
        int bagSize = bag.getTackles().size();
        if (bagSize > index) {
            return bag.getTackles().get(index);
        } else {
            return null;
        }
    }

    /**
     * Вернуть пакет с аммуницией с полоки с индексом index.
     *
     * @param storeHouse
     * @param index индекс полки на складе
     * @return
     */
    public static AmmunitionPacket getAmmunition(StoreHouse storeHouse, int index) {
        AmmunitionBag bag = storeHouse.getAmmunitionBag();
        int min = storeHouse.getBag().getTackles().size();
        if (index >= min && index < bag.getSize() + min) {
            return bag.getPackets().get(index);
        } else {
            return null;
        }
    }

    /**
     * Вернуть количество занятых полок на складе.
     *
     * @param store склад
     * @return размер занимаемый кладью и аммуницией.
     */
    public static int getSize(StoreHouse store) {
        return store.getBag().getTackles().size() + store.getAmmunitionBag().getSize();
    }

    /**
     * Вернуть "вес" склада.
     * @param store склад
     * @return совокупная масса клади и амммуниции на складе.
     */
    public static int getWeight(StoreHouse store) {
        return store.getBag().getWeight() + store.getAmmunitionBag().getWeight();
    }

    /**
     * Вынуть содержимое ячейки(аммуницию) с индексом index со складе storeHouse в количестве amount.
     *
     * @param storeHouse склад
     * @param index индекс ячейки на складе
     * @param amount количество аммуниции
     * @return Вернуть действительно вынутое количество(может быть 0, если в ячейке не аммуниция, а кладь).
     */
    public static int removeAmmunition(StoreHouse storeHouse, int index, int amount) {
        AmmunitionBag bag = storeHouse.getAmmunitionBag();
        index -= storeHouse.getBag().getTackles().size();
        if (index >= 0 && index < bag.getSize()) {
            return AmmunitionBagHelper.putOut(bag, bag.getPackets().get(index).getAmmunition(), amount);
        }
        return 0;
    }

    /**
     * Удалить содержимое склада storeHouse из ячейки index с кладью.
     *
     * @param storeHouse
     * @param index
     * @return  вынутую кладь, если в я чейке не кладь(а аммуниция) или я чейка отсутствует - вернуть null
     */
    public static StateTackle removeStateTackle(StoreHouse storeHouse, int index) {
        StateTackleCollection tackles = storeHouse.getBag().getTackles();
        if (tackles.size() > index) {
            StateTackle tackle = tackles.get(index);
            tackles.remove(index);
            return tackle;
        }
        return null;
    }

}

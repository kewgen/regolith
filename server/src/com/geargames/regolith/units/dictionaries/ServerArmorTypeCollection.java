package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.ArmorType;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ServerArmorTypeCollection extends ArmorTypeCollection {
    private List<ArmorType> armorTypes;

    public List<ArmorType> getArmorTypes() {
        return armorTypes;
    }

    public void setArmorTypes(List<ArmorType> armorTypes) {
        this.armorTypes = armorTypes;
    }

    public ArmorType get(int index) {
        return armorTypes.get(index);
    }

    public void add(ArmorType armorType) {
        armorTypes.add(armorType);
    }

    public void insert(ArmorType armorType, int index) {
        armorTypes.add(index, armorType);
    }

    public void remove(int index) {
        armorTypes.remove(index);
    }

    public int size() {
        return armorTypes.size();
    }

    public void set(ArmorType armorType, int index) {
        armorTypes.set(index, armorType);
    }
}

package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.ArmorType;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ClientArmorTypeCollection extends ArmorTypeCollection {
    private Vector armorTypes;

    public Vector getArmorTypes() {
        return armorTypes;
    }

    public void setArmorTypes(Vector armorTypes) {
        this.armorTypes = armorTypes;
    }

    public ArmorType get(int index) {
        return (ArmorType)armorTypes.elementAt(index);
    }

    public void add(ArmorType armorType) {
        armorTypes.addElement(armorType);
    }

    public void insert(ArmorType armorType, int index) {
        armorTypes.insertElementAt(armorType, index);
    }

    public void remove(int index) {
        armorTypes.removeElementAt(index);
    }

    public int size() {
        return armorTypes.size();
    }

    public void set(ArmorType armorType, int index) {
        armorTypes.setElementAt(armorType, index);
    }
}

package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.WeaponType;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ClientWeaponTypeCollection extends WeaponTypeCollection {

    private Vector weaponTypes;

    public Vector getWeaponTypes() {
        return weaponTypes;
    }

    public void setWeaponTypes(Vector weaponTypes) {
        this.weaponTypes = weaponTypes;
    }

    public WeaponType get(int index) {
        return (WeaponType)weaponTypes.elementAt(index);
    }

    public void add(WeaponType weaponType) {
        weaponTypes.addElement(weaponType);
    }

    public void insert(WeaponType weaponType, int index) {
        weaponTypes.insertElementAt(weaponType, index);
    }

    public void remove(int index) {
        weaponTypes.removeElementAt(index);
    }

    public int size() {
        return weaponTypes.size();
    }

    public void set(WeaponType weaponType, int index) {
        weaponTypes.setElementAt(weaponType, index);
    }
}

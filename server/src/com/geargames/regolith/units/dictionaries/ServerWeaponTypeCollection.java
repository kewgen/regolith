package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.WeaponType;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ServerWeaponTypeCollection extends WeaponTypeCollection {

    private List<WeaponType> weaponTypes;

    public List<WeaponType> getWeaponTypes() {
        return weaponTypes;
    }

    public void setWeaponTypes(List<WeaponType> weaponTypes) {
        this.weaponTypes = weaponTypes;
    }

    public WeaponType get(int index) {
        return weaponTypes.get(index);
    }

    public void add(WeaponType weaponType) {
        weaponTypes.add(weaponType);
    }

    public void insert(WeaponType weaponType, int index) {
        weaponTypes.add(index, weaponType);
    }

    public void remove(int index) {
        weaponTypes.remove(index);
    }

    public int size() {
        return weaponTypes.size();
    }

    public void set(WeaponType weaponType, int index) {
        weaponTypes.set(index, weaponType);
    }
}

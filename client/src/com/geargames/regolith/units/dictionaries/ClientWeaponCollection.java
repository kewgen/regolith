package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Weapon;

import java.util.Vector;

/**
 * @author Mikhail_Kutuzov
 *         created: 30.05.12  12:11
 */
public class ClientWeaponCollection extends WeaponCollection {
    private Vector weapons;

    public Vector getWeapons() {
        return weapons;
    }

    public void setWeapons(Vector weapons) {
        this.weapons = weapons;
    }

    public Weapon get(int index) {
        return (Weapon)weapons.elementAt(index);
    }

    public void add(Weapon weapon) {
        weapons.addElement(weapon);
    }

    public void insert(Weapon weapon, int index) {
        weapons.insertElementAt(weapon, index);
    }

    public void set(Weapon weapon, int index) {
        weapons.setElementAt(weapon, index);
    }

    public void remove(int index) {
        weapons.removeElementAt(index);
    }

    public int size() {
        return weapons.size();
    }
}

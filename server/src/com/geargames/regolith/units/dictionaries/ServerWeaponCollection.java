package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Weapon;

import java.util.List;

/**
 * @author Mikhail_Kutuzov
 *         created: 30.05.12  11:59
 */
public class ServerWeaponCollection extends WeaponCollection {

    private List<Weapon> weapons;

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    @Override
    public Weapon get(int index) {
        return weapons.get(index);
    }

    @Override
    public void add(Weapon weapon) {
        weapons.add(weapon);
    }

    @Override
    public void insert(Weapon weapon, int index) {
        weapons.add(index, weapon);
    }

    @Override
    public void set(Weapon weapon, int index) {
        weapons.set(index, weapon);
    }

    @Override
    public void remove(int index) {
        weapons.remove(index);
    }

    @Override
    public int size() {
        return weapons.size();
    }
}

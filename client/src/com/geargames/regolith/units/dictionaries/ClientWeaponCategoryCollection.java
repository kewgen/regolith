package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.WeaponCategory;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ClientWeaponCategoryCollection extends WeaponCategoryCollection {

    private Vector weaponCategories;

    public ClientWeaponCategoryCollection() {
    }

    public ClientWeaponCategoryCollection(int capacity) {
        weaponCategories = new Vector(capacity);
    }

    public Vector getWeaponCategories() {
        return weaponCategories;
    }

    public void setWeaponCategories(Vector weaponCategories) {
        this.weaponCategories = weaponCategories;
    }

    public WeaponCategory get(int index) {
        return (WeaponCategory)weaponCategories.get(index);
    }

    public void add(WeaponCategory weaponCategory) {
        weaponCategories.addElement(weaponCategory);
    }

    public void insert(WeaponCategory weaponCategory, int index) {
        weaponCategories.insertElementAt(weaponCategory, index);
    }

    public void remove(int index) {
        weaponCategories.removeElementAt(index);
    }

    public int size() {
        return weaponCategories.size();
    }

    public void set(WeaponCategory weaponCategory, int index) {
        weaponCategories.setElementAt(weaponCategory, index);
    }
}

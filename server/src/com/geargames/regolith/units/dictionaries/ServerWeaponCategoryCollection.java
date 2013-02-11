package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.WeaponCategory;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ServerWeaponCategoryCollection extends WeaponCategoryCollection {
    private List<WeaponCategory> categories;

    public List<WeaponCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<WeaponCategory> categories) {
        this.categories = categories;
    }

    public WeaponCategory get(int index) {
        return categories.get(index);
    }

    public void add(WeaponCategory weaponCategory) {
        categories.add(weaponCategory);
    }

    public void insert(WeaponCategory weaponCategory, int index) {
        categories.add(index, weaponCategory);
    }

    public void remove(int index) {
        categories.remove(index);
    }

    public int size() {
        return categories.size();
    }

    public void set(WeaponCategory weaponCategory, int index) {
        categories.set(index, weaponCategory);
    }
}

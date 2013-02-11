package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.AmmunitionCategory;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ServerAmmunitionCategoryCollection extends AmmunitionCategoryCollection {
    private List<AmmunitionCategory> categories;

    public List<AmmunitionCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<AmmunitionCategory> categories) {
        this.categories = categories;
    }

    public AmmunitionCategory get(int index) {
        return categories.get(index);
    }

    public void add(AmmunitionCategory category) {
        categories.add(category);
    }

    public void insert(AmmunitionCategory category, int index) {
        categories.add(index, category);
    }

    public void remove(int index) {
        categories.remove(index);
    }

    public int size() {
        return categories.size();
    }

    public void set(AmmunitionCategory category, int index) {
        categories.set(index, category);
    }
}

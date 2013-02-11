package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.AmmunitionCategory;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public class ClientAmmunitionCategoryCollection extends AmmunitionCategoryCollection {

    private Vector categories;

    public Vector getCategories() {
        return categories;
    }

    public void setCategories(Vector categories) {
        this.categories = categories;
    }

    public AmmunitionCategory get(int index) {
        return (AmmunitionCategory)categories.elementAt(index);
    }

    public void add(AmmunitionCategory category) {
        categories.addElement(category);
    }

    public void insert(AmmunitionCategory category, int index) {
        categories.insertElementAt(category, index);
    }

    public void remove(int index) {
        categories.removeElementAt(index);
    }

    public int size() {
        return categories.size();
    }

    public void set(AmmunitionCategory category, int index) {
        categories.setElementAt(category, index);
    }
}

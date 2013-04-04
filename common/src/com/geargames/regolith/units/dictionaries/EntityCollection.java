package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Entity;

import java.io.Serializable;

/**
 * Users: mkutuzov, abarakov
 * Date: 26.04.12
 */
public abstract class EntityCollection implements Serializable {
    public abstract Entity get(int index);
    public abstract void remove(int index);
    public abstract int size();

    public int indexById(int id) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

}

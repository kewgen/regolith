package com.geargames.regolith.units.dictionaries;


import com.geargames.regolith.units.Entity;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public abstract class EntityCollection implements Serializable {
    public abstract Entity get(int index);
    public abstract void remove(int index);
    public abstract int size();
}

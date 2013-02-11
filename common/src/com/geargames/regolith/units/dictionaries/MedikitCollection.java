package com.geargames.regolith.units.dictionaries;


import com.geargames.regolith.units.tackle.Medikit;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public abstract class MedikitCollection extends EntityCollection {
    public abstract Medikit get(int index);
    public abstract void add(Medikit medikit);
    public abstract void insert(Medikit medikit, int index);
    public abstract void set(Medikit medikit, int index);
}

package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Projectile;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public abstract class ProjectileCollection extends EntityCollection {
    public abstract Projectile get(int index);
    public abstract void add(Projectile projectile);
    public abstract void insert(Projectile projectile, int index);
    public abstract void set(Projectile projectile, int index);
}

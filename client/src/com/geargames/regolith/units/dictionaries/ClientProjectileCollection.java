package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Projectile;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ClientProjectileCollection extends ProjectileCollection {
    private Vector projectiles;

    public Vector getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(Vector projectiles) {
        this.projectiles = projectiles;
    }

    public Projectile get(int index) {
        return (Projectile)projectiles.elementAt(index);
    }

    public void add(Projectile projectile) {
        projectiles.addElement(projectile);
    }

    public void insert(Projectile projectile, int index) {
        projectiles.insertElementAt(projectile, index);
    }

    public void remove(int index) {
        projectiles.removeElementAt(index);
    }

    public int size() {
        return projectiles.size();
    }

    public void set(Projectile projectile, int index) {
        projectiles.setElementAt(projectile, index);
    }
}

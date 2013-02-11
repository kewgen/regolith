package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Projectile;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ServerProjectileCollection extends ProjectileCollection {

    private List<Projectile> projectiles;

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public Projectile get(int index) {
        return projectiles.get(index);
    }

    public void add(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void insert(Projectile projectile, int index) {
        projectiles.add(index, projectile);
    }

    public void remove(int index) {
        projectiles.remove(index);
    }

    public int size() {
        return projectiles.size();
    }

    public void set(Projectile projectile, int index) {
        projectiles.set(index, projectile);
    }
}

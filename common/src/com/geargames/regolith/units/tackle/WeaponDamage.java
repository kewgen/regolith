package com.geargames.regolith.units.tackle;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 03.02.12
 * Time: 17:24
 * Класс содержит координаты по оси урона, трапеции "урон-расстояние".
 */
public class WeaponDamage implements Serializable {
    private short minDistance;
    private short optDistance;
    private short maxDistance;

    public short getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(short minDistance) {
        this.minDistance = minDistance;
    }

    public short getOptDistance() {
        return optDistance;
    }

    public void setOptDistance(short optDistance) {
        this.optDistance = optDistance;
    }

    public short getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(short maxDistance) {
        this.maxDistance = maxDistance;
    }
}

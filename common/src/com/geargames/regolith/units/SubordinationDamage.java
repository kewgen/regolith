package com.geargames.regolith.units;

/**
 * User: mkutuzov
 * Date: 09.02.12
 */
public class SubordinationDamage extends Entity {
    private byte minRankDifference;
    private byte maxRankDifference;
    private short damage;

    public byte getMaxRankDifference() {
        return maxRankDifference;
    }

    public void setMaxRankDifference(byte maxRankDifference) {
        this.maxRankDifference = maxRankDifference;
    }

    public byte getMinRankDifference() {
        return minRankDifference;
    }

    public void setMinRankDifference(byte minRankDifference) {
        this.minRankDifference = minRankDifference;
    }

    public short getDamage() {
        return damage;
    }

    public void setDamage(short damage) {
        this.damage = damage;
    }
}

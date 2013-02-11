package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.Entity;

/**
 * User: mkutuzov
 * Date: 08.02.12
 */
public class AmmunitionCategory extends Entity{
    private String name;
    private double quality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }
}

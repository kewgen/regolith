package com.geargames.regolith.units.tackle;

/**
 * User: mkutuzov
 * Date: 06.02.12
 */
public abstract class Tackle extends AbstractTackle {
    private int frameId;
    private String name;
    private short weight;

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }
}

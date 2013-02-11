package com.geargames.regolith.units;

/**
 * User: mkutuzov
 * Date: 02.04.12
 */
public class Underload extends Entity {
    private byte min;
    private byte max;
    private byte action;

    public byte getMin() {
        return min;
    }

    public void setMin(byte min) {
        this.min = min;
    }

    public byte getMax() {
        return max;
    }

    public void setMax(byte max) {
        this.max = max;
    }

    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }
}

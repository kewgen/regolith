package com.geargames.regolith.units.tackle;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 03.02.12
 * Класс содержит в себе координаты по оси расстояния, вершин трапеции "урон-расстояние".
  */
public class WeaponDistances implements Serializable {
    private byte min;
    private byte max;
    private byte minOptimal;
    private byte maxOptimal;

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

    public byte getMinOptimal() {
        return minOptimal;
    }

    public void setMinOptimal(byte minOptimal) {
        this.minOptimal = minOptimal;
    }

    public byte getMaxOptimal() {
        return maxOptimal;
    }

    public void setMaxOptimal(byte maxOptimal) {
        this.maxOptimal = maxOptimal;
    }
}

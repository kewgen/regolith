package com.geargames.regolith.units.map;

import com.geargames.regolith.units.Entity;

/**
 * User: mkutuzov
 * Date: 07.03.12
 */
public class ExitZone extends Entity {
    private short x;
    private short y;
    private byte xRadius;
    private byte yRadius;

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public byte getxRadius() {
        return xRadius;
    }

    public void setxRadius(byte xRadius) {
        this.xRadius = xRadius;
    }

    public byte getyRadius() {
        return yRadius;
    }

    public void setyRadius(byte yRadius) {
        this.yRadius = yRadius;
    }

    public boolean  isWithIn(int xx, int yy){
        return x - xRadius <= xx && y - yRadius <= yy && xx <= x + xRadius && yy <= y + yRadius;
    }
}

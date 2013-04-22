package com.geargames.regolith.units.map;

import com.geargames.regolith.units.Entity;

/**
 * Класс реализующий зону высадки и зону посадки (эвакуации).
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

    /**
     * Получить радиус зоны высадки/посадки по горизонтали. Значение 1, например, означает, что зона высадки/посадки
     * будет шириной 3 клетки, т.е. центр зоны + по одной клетке влево и вправо.
     * @return
     */
    public byte getxRadius() {
        return xRadius;
    }

    public void setxRadius(byte xRadius) {
        this.xRadius = xRadius;
    }

    /**
     * Получить радиус зоны высадки/посадки по вертикали. Значение 1, например, означает, что зона высадки/посадки
     * будет высотой 3 клетки, т.е. центр зоны + по одной клетке вверх и вниз.
     * @return
     */
    public byte getyRadius() {
        return yRadius;
    }

    public void setyRadius(byte yRadius) {
        this.yRadius = yRadius;
    }

    public boolean isWithIn(int xx, int yy){
        return x - xRadius <= xx && xx <= x + xRadius && y - yRadius <= yy && yy <= y + yRadius;
    }

    @Override
    public String toString() {
        return super.toString() + "; x=" + x + "; y=" + y + "; xR=" + xRadius + "; yR=" + yRadius;
    }

}

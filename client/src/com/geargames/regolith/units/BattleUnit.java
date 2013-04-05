package com.geargames.regolith.units;

/**
 * Боец постоянно движется по игровой сетке координат(над которой движется экран), эта сущность, какраз, предназначена
 * для связи бойца с этой сеткой.
 * User: mkutuzov
 * Date: 24.02.12
 */
public class BattleUnit {
    private int mapX;
    private int mapY;
    private Unit unit;

    /**
     * Вернуть координату на игровой карте по горизонтальной оси.
     *
     * @return
     */
    public int getMapX() {
        return mapX;
    }

    public void setMapX(int mapX) {
        this.mapX = mapX;
    }

    /**
     * Вернуть координату на игровой карте по вертикальной оси.
     *
     * @return
     */
    public int getMapY() {
        return mapY;
    }

    public void setMapY(int mapY) {
        this.mapY = mapY;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}

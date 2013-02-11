package com.geargames.regolith.units;

import com.geargames.regolith.units.battle.Warrior;

/**
 * Боец постоянно движется по игровой сетке координат(над которой движется экран), эта сущность, какраз, предназначена
 * для связи бойца с этой сеткой.
 * User: mkutuzov
 * Date: 24.02.12
 */
public class Unit {
    private int mapX;
    private int mapY;
    private Warrior warrior;

    /**
     * Вернуть координату на игровой карте по горизонтальной оси.
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
     * @return
     */
    public int getMapY() {
        return mapY;
    }

    public void setMapY(int mapY) {
        this.mapY = mapY;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }
}

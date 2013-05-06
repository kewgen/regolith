package com.geargames.regolith.units.map;

import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.battle.Direction;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: abarakov
 * Date: 30.04.13
 * <p/>
 * Класс бойца, расположенного в одной из клеток на карте.
 */
public abstract class HumanElement extends DynamicCellElement {
    private Human human;
    private short cellX;
    private short cellY;
    private short mapX; //todo: два поля mapX и mapY только для ClientHumanElement
    private short mapY;
    private Direction direction;
    private boolean sitting;

    //todo: поле Id из класса Entity в классе HumanElement не нужно

    public HumanElement() {
        human = null;
        cellX = 0; //todo: 0 -> -32768
        cellY = 0; //todo: 0 -> -32768
        mapX = -32768;
        mapY = -32768;
        direction = Direction.NONE;
        sitting = false;
    }

    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    //todo: там где апдейтятся cellX и cellY, возможно, следует апдейтить mapX и mapY

    /**
     * Получить номер ячейки по оси X, в которой расположен боец.
     *
     * @return
     */
    public short getCellX() {
        return cellX;
    }

    public void setCellX(short x) {
        this.cellX = x;
    }

    /**
     * Получить номер ячейки по оси Y, в которой расположен боец.
     *
     * @return
     */
    public short getCellY() {
        return cellY;
    }

    public void setCellY(short y) {
        this.cellY = y;
    }

    /**
     * Получить координаты бойца на карте по оси X.
     *
     * @return
     */
    public short getMapX() {
        return mapX;
    }

    public void setMapX(short x) {
        this.mapX = x;
    }

    /**
     * Получить координаты бойца на карте по оси Y.
     *
     * @return
     */
    public short getMapY() {
        return mapY;
    }

    public void setMapY(short y) {
        this.mapY = y;
    }

    /**
     * Получить направление, в котором повёрнут боец.
     *
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Вернет true, если боец сидит.
     *
     * @return
     */
    public boolean isSitting() {
        return sitting;
    }

    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    @Override
    public int getFrameId() {
        return -1;
    }

    @Override
    public boolean isHalfLong() {
        return sitting;
    }

    @Override
    public boolean isAbleToLookThrough() {
        return sitting;
    }

    @Override
    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return sitting;
    }

    @Override
    public boolean isAbleToWalkThrough() {
        return false;
    }

    @Override
    public boolean isBarrier() {
        return false;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.HUMAN;
    }

    @Override
    public byte getLayer() {
        return CellElementLayers.HUMAN;
    }

}

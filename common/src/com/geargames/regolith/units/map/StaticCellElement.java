package com.geargames.regolith.units.map;

import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: abarakov
 * Date: 19.04.13
 * Базовый класс для всех статических сущностей располагающихся в ячейках карты.
 */
public abstract class StaticCellElement extends CellElement {

    /**
     * Вернуть идентификатор фрейма для данного объекта карты.
     *
     * @return
     */
    public abstract int getFrameId();

    /**
     * Возможно ли видеть через этот элемент?
     */
    @Override
    public boolean isAbleToLookThrough() {
        return true;
    }

    /**
     * Возможно ли ходить через этот элемент?
     */
    @Override
    public boolean isAbleToWalkThrough() {
        return true;
    }

    /**
     * Возможно ли стрелять через этот элемент оружием данного вида?
     */
    @Override
    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return true;
    }

    /**
     * Это препятствие в половину роста бойца?
     */
    @Override
    public boolean isHalfLong() {
        return false;
    }

    /**
     * Вернет true, если элемент является барьером.
     */
    @Override
    public boolean isBarrier() {
        return false;
    }

    /**
     * Получить тип элемента.
     *
     * @return - одно из значений CellElementTypes
     */
    @Override
    public short getElementType() {
        return CellElementTypes.STATIC;
    }

    /**
     * Получить номер слоя, в котором должен располагаться элемент ячейки.
     *
     * @return - одно из значений CellElementLayers
     */
    @Override
    public byte getLayer() {
        return CellElementLayers.STATIC;
    }

}

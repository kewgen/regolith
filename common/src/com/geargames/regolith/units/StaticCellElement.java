package com.geargames.regolith.units;

import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: abarakov
 * Date: 19.04.13
 * Базовый класс для всех статических сущностей располагающихся в ячейках карты.
 */
public abstract class StaticCellElement extends CellElement {

    /**
     * Вернуть идентификатор фрейма для данного объекта карты.
     * @return
     */
    public abstract int getFrameId();

    /**
     * Возможно ли видеть через этот элемент?
     */
    public abstract boolean isAbleToLookThrough();

    /**
     * Возможно ли ходить через этот элемент?
     */
    public boolean isAbleToWalkThrough() {
        return true;
    }

    /**
     * Возможно ли стрелять через этот элемент оружием данного вида?
     */
    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return true;
    }

    /**
     * Это препятствие в половину роста бойца?
     */
    public boolean isHalfLong() {
        return false;
    }

    /**
     * Получить тип элемента.
     * @return - одно из значений CellElementTypes
     */
    public short getElementType() {
        return CellElementTypes.STATIC;
    }

    /**
     * Получить номер слоя, в котором должен располагаться элемент ячейки.
     * @return - одно из значений CellElementLayers
     */
    public byte getLayer() {
        return CellElementLayers.STATIC;
    }

}

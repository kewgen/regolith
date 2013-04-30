package com.geargames.regolith.units;

import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * Users: mkutuzov, abarakov
 * Date: 06.02.12
 * Базовый класс для всех сущностей располагающихся в ячейках карты.
 */
public abstract class CellElement extends Entity {

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
    public abstract boolean isAbleToWalkThrough();

    /**
     * Возможно ли стрелять через этот элемент оружием данного вида?
     */
    public abstract boolean isAbleToShootThrough(WeaponCategory weaponCategory);

    /**
     * Это препятствие в половину роста бойца?
     */
    public abstract boolean isHalfLong();

    /**
     * Вернет true, если элемент является барьером.
     */
    public abstract boolean isBarrier();

    /**
     * Получить тип элемента.
     * @return - одно из значений CellElementTypes
     */
    public abstract short getElementType();

    /**
     * Получить номер слоя, в котором должен располагаться элемент ячейки.
     * @return - одно из значений CellElementLayers
     */
    public abstract byte getLayer();

}

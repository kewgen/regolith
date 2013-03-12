package com.geargames.regolith.units;

import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 06.02.12
 * Все сущности располагающиеся на клетках сетки карты, должны наследовать этот класс.
  */
public abstract class Element extends Entity {
    /**
     * Вернуть идентификатор фрейма для даного объекта карты.
     * @return
     */
    public abstract int getFrameId();
    /**
     * Возможно ли видеть через это?
     */
    public abstract boolean isAbleToLookThrough();
    /**
     * Возможно ли ходить через это?
     */
    public abstract boolean isAbleToWalkThrough();
    /**
     * Возможно ли стрелять через это оружием даного вида?
     */
    public abstract boolean isAbleToShootThrough(WeaponCategory weaponCategory);
    /**
     * Это препятствие в половину роста бойца?
     */
    public abstract boolean isHalfLong();
}

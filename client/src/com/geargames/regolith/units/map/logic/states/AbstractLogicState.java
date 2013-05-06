package com.geargames.regolith.units.map.logic.states;

import com.geargames.regolith.units.map.DynamicCellElement;

/**
 * User: abarakov
 * Date: 01.05.13
 */
// AbstractUnitState
public abstract class AbstractLogicState {

    /**
     * Получить тип действия.
     *
     * @return
     */
    public abstract byte getAction();

    /**
     * Перейти к выполнению следующего стейта бойца.
     *
     * @param owner
     * @param newState
     * @return
     */
    public abstract void change(DynamicCellElement owner, AbstractLogicState newState);

    /**
     * Запустить выполнение действия.
     *
     * @param owner
     */
    public abstract void start(DynamicCellElement owner);

    /**
     * Остановить выполнение действия.
     *
     * @param owner
     */
    public abstract void stop(DynamicCellElement owner);

    /**
     * @param owner
     * @return вернет true, если выполнение действия было завершено.
     */
    public abstract boolean onTick(DynamicCellElement owner);

}

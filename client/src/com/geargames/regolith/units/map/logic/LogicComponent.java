package com.geargames.regolith.units.map.logic;

import com.geargames.common.util.ArrayList;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.map.Tickable;
import com.geargames.regolith.units.map.logic.states.AbstractLogicState;

/**
 * Логическая компонента динамического элемента на карте.
 * User: abarakov
 * Date: 01.05.13
 */
// Logical Component
public abstract class LogicComponent implements Tickable {
    private DynamicCellElement owner;
    private ArrayList queueStates; //todo: это вообще, кроме бойцов, кому-то еще нужно?
    private AbstractLogicState currentState;

    public LogicComponent(DynamicCellElement owner) {
        this.owner = owner;
        queueStates = new ArrayList(getQueueCapacityDefault());
        currentState = null;
    }

    protected abstract int getQueueCapacityDefault();

    /**
     * Получить ссылку на динамический элемент карты являющегося владельцем компонента логики.
     *
     * @return
     */
    public DynamicCellElement getOwner() {
        return owner;
    }

    /**
     * Получить текущий стейт, в котором находится динамический элемент карты.
     *
     * @return
     */
    public AbstractLogicState getCurrentState() {
        return currentState;
    }

    /**
     * Получить последний стейт, в котором динамический элемент карты будет находится при завершении выполнения очереди команд.
     *
     * @return
     */
    public AbstractLogicState getFinishState() {
        if (queueStates.size() > 0) {
            return (AbstractLogicState) queueStates.get(queueStates.size() - 1);
        } else {
            return currentState;
        }
    }

    /**
     * Получить начальный стейт, в котором динамический элемент карты должен находиться в первоначальном состоянии.
     * Обычно устанавливается при загрузке игровой карты или при восстановлении снапшота битвы после сбоя связи.
     *
     * @return
     */
    protected abstract AbstractLogicState getInitialState();

    /**
     * Получить стейт, в котором динамический элемент карты должен находиться в состоянии покоя. Стейт может быть разным
     * в определенные моменты битвы и может зависеть от текущего состояния элемента.
     *
     * @return
     */
    protected abstract AbstractLogicState getIdleState();

    /**
     * Установить новый стейт для динамического элемента карты.
     *
     * @param state
     */
    private void setState(AbstractLogicState state) {
        if (currentState != state) {
            if (currentState != null) {
                currentState.stop(owner);
            }
            currentState = state;
            currentState.start(owner);
        }
    }

    /**
     * Установить начальный стейт. Обычно устанавливается при загрузке игровой карты или при восстановлении снапшота
     * битвы после сбоя связи.
     */
    public void initiate() {
        setState(getInitialState());
    }

    public void changeState(AbstractLogicState state) {
        getFinishState().change(getOwner(), state);
    }

    /**
     * Проверить, находится ли динамический элемент на карте в состоянии покоя.
     *
     * @return вернет true, если динамический элемент ничего не выполняет в данный момент.
     */
    public abstract boolean isIdle();

    /**
     * Поместить в очередь выполнения новую команду.
     *
     * @param state
     */
    public void pushState(AbstractLogicState state) {
        queueStates.add(state);
    }

    /**
     * Извлечь из очереди выполнения следующую команду.
     */
    private void popState() {
        if (queueStates.size() > 0) {
            setState((AbstractLogicState) queueStates.remove(0));
        } else {
            setState(getIdleState());
            onFinish();
        }
    }

    /**
     * Выполнить обновление состояния динамического элемента карты.
     */
    @Override
    public void onTick() {
        if (currentState.onTick(owner)) {
            popState();
        }
    }

    /**
     * Обработчик события завершения выполнения очереди команд. Динамический элемент карты перешел в состояние покоя.
     */
    protected abstract void onFinish();

    /**
     * Быстро завершить выполнение всех команд.
     */
    public void quicklyCompleteAllCommands() {
        while (!isIdle()) {
            onTick();
        }
    }

}

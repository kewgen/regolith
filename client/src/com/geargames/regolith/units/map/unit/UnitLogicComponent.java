package com.geargames.regolith.units.map.unit;

import com.geargames.regolith.units.map.ClientWarriorElement;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.map.LogicComponent;
import com.geargames.regolith.units.map.unit.states.*;

/**
 * User: abarakov
 * Date: 30.04.13
 * <p/>
 * Класс логики бойца. Служит для выполнения атомарных действий, таких как, двигаться, присесть, выстрелить, умереть и т.д.
 */
public class UnitLogicComponent extends LogicComponent {
    private static UnitStandState standState = new UnitStandState();       // Боец стоит
    private static UnitSitState sitState = new UnitSitState();             // Боец сидит
    private static UnitSitDownState sitDownState = new UnitSitDownState(); // Боец садится
    private static UnitStandUpState standUpState = new UnitStandUpState(); // Боец встает
    private UnitRunState runState;                                         // Боец бежит
    private static UnitStandSimpleState standAndHastilyShotState = new UnitStandSimpleState(Actions.HUMAN_STAND_AND_SHOOT);  // Боец делает выстрел "наспех" стоя
    private static UnitSitSimpleState sitAndHastilyShotState = new UnitSitSimpleState(Actions.HUMAN_SIT_AND_SHOOT);          // Боец делает выстрел "наспех" сидя
    private static UnitStandSimpleState standAndAccurateShotState = new UnitStandSimpleState(Actions.HUMAN_STAND_AND_SHOOT); // Боец делает прицельный выстрел стоя
    private static UnitSitSimpleState sitAndAccurateShotState = new UnitSitSimpleState(Actions.HUMAN_SIT_AND_SHOOT);         // Боец делает прицельный выстрел сидя
    private static UnitStandSimpleState standAndHitState = new UnitStandSimpleState(Actions.HUMAN_STAND_AND_HIT);            // Боец получает урон стоя
    private static UnitSitSimpleState sitAndHitState = new UnitSitSimpleState(Actions.HUMAN_SIT_AND_HIT);                    // Боец получает урон сидя
    private static UnitStandSimpleState standAndDieState = new UnitStandSimpleState(Actions.HUMAN_STAND_AND_DIE);            // Боец умирает стоя
    private static UnitSitSimpleState sitAndDieState = new UnitSitSimpleState(Actions.HUMAN_SIT_AND_DIE);                    // Боец умирает сидя

    public UnitLogicComponent(DynamicCellElement owner) {
        super(owner);
        runState = new UnitRunState();
    }

    @Override
    protected int getQueueCapacityDefault() {
        return 4;
    }

    @Override
    public ClientWarriorElement getOwner() {
        return (ClientWarriorElement) super.getOwner();
    }

    @Override
    protected AbstractLogicState getInitialState() {
        return standState;
    }

    @Override
    protected AbstractLogicState getIdleState() {
        if (getOwner().isSitting()) {
            return sitState;
        } else {
            return standState;
        }
    }

    @Override
    public boolean isIdle() {
        AbstractLogicState state = getCurrentState();
        return state == standState || state == sitState;
    }

    /**
     * Обработчик события завершения выполнения очереди команд. Боец перешел в состояние покоя.
     */
    @Override
    protected void onFinish() {
        //todo: оповестить об этом BattleScreen
    }

    /**
     * Приказать бойцу присесть.
     */
    public void doSitDown() {
        changeState(sitDownState);
    }

    /**
     * Приказать бойцу встать.
     */
    public void doStandUp() {
        changeState(standUpState);
    }

    /**
     * Приказать бойцу начать движение.
     */
    public void doRun() {
        changeState(runState);
    }

    /**
     * Приказать бойцу выстрелить "наспех".
     */
    public void doHastilyShot() {
        if (getOwner().isSitting()) {
            changeState(sitAndHastilyShotState);
        } else {
            changeState(standAndHastilyShotState);
        }
    }

    /**
     * Приказать бойцу прицельно выстрелить.
     */
    public void doAccurateShot() {
        if (getOwner().isSitting()) {
            changeState(sitAndAccurateShotState);
        } else {
            changeState(standAndAccurateShotState);
        }
    }

    /**
     * Приказать бойцу получить урон.
     */
    public void doHit() {
        if (getOwner().isSitting()) {
            changeState(sitAndHitState);
        } else {
            changeState(standAndHitState);
        }
    }

    /**
     * Приказать бойцу умереть.
     */
    public void doDie() {
        if (getOwner().isSitting()) {
            changeState(sitAndDieState);
        } else {
            changeState(standAndDieState);
        }
    }

    public static UnitStandState getStandState() {
        return standState;
    }

    public static UnitSitState getSitState() {
        return sitState;
    }

    public static UnitSitDownState getSitDownState() {
        return sitDownState;
    }

    public static UnitStandUpState getStandUpState() {
        return standUpState;
    }

    public UnitRunState getRunState() {
        return runState;
    }

    public static UnitStandSimpleState getStandAndHastilyShotState() {
        return standAndHastilyShotState;
    }

    public static UnitSitSimpleState getSitAndHastilyShotState() {
        return sitAndHastilyShotState;
    }

    public static UnitStandSimpleState getStandAndAccurateShotState() {
        return standAndAccurateShotState;
    }

    public static UnitSitSimpleState getSitAndAccurateShotState() {
        return sitAndAccurateShotState;
    }

    public static UnitStandSimpleState getStandAndHitState() {
        return standAndHitState;
    }

    public static UnitSitSimpleState getSitAndHitState() {
        return sitAndHitState;
    }

    public static UnitStandSimpleState getStandAndDieState() {
        return standAndDieState;
    }

    public static UnitSitSimpleState getSitAndDieState() {
        return sitAndDieState;
    }

}

package com.geargames.regolith.units.map.logic.states;

import com.geargames.common.logging.Debug;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.battle.Human;
import com.geargames.regolith.units.battle.Direction;
import com.geargames.regolith.units.map.AbstractClientWarriorElement;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.map.states.Actions;

/**
 * Users: abarakov, mkutuzov
 * Date: 01.05.13
 * <p/>
 * Боец находится в движении в назначенную точку на карте.
 * Задача этого класса - совместить раздельные шаги по клеткам карты и обозрение окружающих ячеек с плавным перемещением
 * бойца по игровому полю.
 */
public class UnitRunState extends AbstractLogicState {
    private Direction stepDirection;
    private int ticks;

    private int beginMapX;
    private int beginMapY;
    private int extensionX;
    private int extensionY;

    private int shiftOnTickX;
    private int shiftOnTickY;
    private int speed;

    @Override
    public byte getAction() {
        return Actions.HUMAN_RUN;
    }

    @Override
    public void change(DynamicCellElement owner, AbstractLogicState newState) {
        switch (newState.getAction()) {
            case Actions.HUMAN_STAND:
            case Actions.HUMAN_RUN:
            case Actions.HUMAN_STAND_AND_SHOOT:
            case Actions.HUMAN_SIT_DOWN:
//            case Actions.HUMAN_SIT:
//            case Actions.HUMAN_SIT_AND_SHOOT:
//            case Actions.HUMAN_SIT_AND_HIT:
//            case Actions.HUMAN_STAND_UP:
            case Actions.HUMAN_STAND_AND_HIT:
            case Actions.HUMAN_STAND_AND_DIE:
//            case Actions.HUMAN_DIE:
//            case Actions.HUMAN_SIT_AND_DIE:
                break;
            default:
                Debug.critical("Invalid state transition from '" + getAction() + "' to '" + newState.getAction() + "'");
                return;
        }
        AbstractClientWarriorElement warrior = (AbstractClientWarriorElement) owner;
        warrior.getLogic().pushState(newState);
    }

    @Override
    public void onStart(DynamicCellElement owner) {
        AbstractClientWarriorElement warrior = (AbstractClientWarriorElement) owner;
        warrior.getGraphic().start(warrior, getAction());

        speed = ClientConfigurationFactory.getConfiguration().getBattleConfiguration().getWalkSpeed();
        shiftOnTickX = BattleScreen.HORIZONTAL_RADIUS / speed;
        shiftOnTickY = BattleScreen.VERTICAL_RADIUS / speed;

        ticks = 0;
        extensionX = 0;
        extensionY = 0;
        beginMapX = warrior.getMapX();
        beginMapY = warrior.getMapY();

////        if (hasToStop(warrior)) {
////            isMoving = false;
////            battleUnit.getUnit().stop();
////        } else {
//
//            stepDirection = WarriorHelper.getStepDirection(PRegolithPanelManager.getInstance().getBattleScreen().getBattle().getMap().getCells(), unit);
//            if (stepDirection == Direction.NONE) {
////                isMoving = false;
////                battleUnit.getUnit().stop();
//            } else {
//                if (stepDirection != unit.getDirection()) {
//                    unit.getGraphic().stop();
//                    unit.setDirection(stepDirection);
//                    unit.getGraphic().start(unit, getAction());
//                }
//            }
////        }
//
//        if (screen.getActiveUnit() == unit) {
//            routeMap(warrior);
//        }
    }

    @Override
    public void onStop(DynamicCellElement owner) {

    }

    /**
     * В каждый цикл игрового обновления дёргаем это метод.
     * Заданное число тиков боец движется от середины одной ячейки к середине следующей,
     * по окончании: мы определяемся со следующей ячейкой и исправляем возможные ошибки, рисуя бойца точно
     * посередине очередной клетки.
     */
    @Override
    public boolean onTick(DynamicCellElement owner) {
        AbstractClientWarriorElement warrior = (AbstractClientWarriorElement) owner;

        boolean needStoped = false;
        if (speed - ticks > 1) {
            extensionX += shiftOnTickX * (stepDirection.getX() - stepDirection.getY());
            extensionY += shiftOnTickY * (stepDirection.getY() + stepDirection.getX());
            warrior.setMapX((short) (extensionX + beginMapX));
            warrior.setMapY((short) (extensionY + beginMapY));
            ticks++;
        } else {
            warrior.setMapX((short) (BattleScreen.HORIZONTAL_RADIUS * (stepDirection.getX() - stepDirection.getY()) + beginMapX));
            warrior.setMapY((short) (BattleScreen.VERTICAL_RADIUS * (stepDirection.getY() + stepDirection.getX()) + beginMapY));

            BattleCell[][] cells = PRegolithPanelManager.getInstance().getBattleScreen().getBattle().getMap().getCells();
            if (warrior.getMembershipType() == Human.ENEMY) {
                BattleMapHelper.resetShortestCell(cells[warrior.getCellX()][warrior.getCellY()], warrior);
                WarriorHelper.putWarriorIntoMap(cells, warrior, warrior.getCellX() + stepDirection.getX(), warrior.getCellY() + stepDirection.getY());
            } else {
                WarriorHelper.step(cells, warrior, stepDirection.getX(), stepDirection.getY(),
                        ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
            }

            stepDirection = WarriorHelper.getStepDirection(cells, warrior);
            if (stepDirection == Direction.NONE) {
                needStoped = true;
                warrior.getGraphic().stop();
//                isMoving = false;
//                battleUnit.getUnit().stop();
            } else {
                if (stepDirection != warrior.getDirection()) {
                    warrior.getGraphic().stop();
                    warrior.setDirection(stepDirection);
                    warrior.getGraphic().start(warrior, getAction());
                }
            }
        }

        warrior.getGraphic().onTick();
        return needStoped;
    }

}

package com.geargames.regolith.units.map.unit.states;

import com.geargames.common.logging.Debug;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientBattleContext;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Direction;
import com.geargames.regolith.units.map.AbstractClientWarriorElement;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.map.WarriorMembershipType;
import com.geargames.regolith.units.map.unit.Actions;

/**
 * Users: abarakov, mkutuzov
 * Date: 01.05.13
 * <p/>
 * Боец находится в движении в назначенную точку на карте.
 * Задача этого класса - совместить раздельные шаги по клеткам карты и обозрение окружающих ячеек с плавным перемещением
 * бойца по игровому полю.
 */
public class UnitRunState extends AbstractLogicState {
    private BattleCell[][] cells;
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

        ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
        cells = configuration.getBattleContext().getBattle().getMap().getCells();
        speed = configuration.getBattleConfiguration().getWalkSpeed();
        shiftOnTickX = ClientBattleContext.HORIZONTAL_RADIUS / speed;
        shiftOnTickY = ClientBattleContext.VERTICAL_RADIUS / speed;

        startStep(warrior);
    }

    @Override
    public void onStop(DynamicCellElement owner) {
        AbstractClientWarriorElement warrior = (AbstractClientWarriorElement) owner;
        if (warrior.getMembershipType() == WarriorMembershipType.WARRIOR) {
            BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
            ClientBattleHelper.route(cells, warrior, battleConfiguration.getRouter(), battleConfiguration);
        }
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

        if (speed - ticks > 1) {
            // Движение от центра одной клетки до центра другой клетки карты
            extensionX += shiftOnTickX * (stepDirection.getY() - stepDirection.getX());
            extensionY += shiftOnTickY * (stepDirection.getX() + stepDirection.getY());
            warrior.setMapX((short) (extensionX + beginMapX));
            warrior.setMapY((short) (extensionY + beginMapY));
            ticks++;
        } else {
            // Мы, приблезительно, в центре одной из клеток карты
            //todo: использовать battleScreen.coordinateFinder для вычисления положения бойца
            warrior.setMapX((short) (ClientBattleContext.HORIZONTAL_RADIUS * (stepDirection.getY() - stepDirection.getX()) + beginMapX));
            warrior.setMapY((short) (ClientBattleContext.VERTICAL_RADIUS * (stepDirection.getX() + stepDirection.getY()) + beginMapY));

            if (warrior.getMembershipType() == WarriorMembershipType.ENEMY) {
                BattleMapHelper.resetShortestCell(cells[warrior.getCellX()][warrior.getCellY()], warrior);
                WarriorHelper.putWarriorIntoMap(cells, warrior, warrior.getCellX() + stepDirection.getX(), warrior.getCellY() + stepDirection.getY());
            } else {
                WarriorHelper.step(cells, warrior, stepDirection.getX(), stepDirection.getY(),
                        ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
            }
            return startStep(warrior);
        }
        return false;
    }

    private boolean startStep(AbstractClientWarriorElement warrior) {
        ticks = 0;
        extensionX = 0;
        extensionY = 0;
        beginMapX = warrior.getMapX();
        beginMapY = warrior.getMapY();

        stepDirection = WarriorHelper.getStepDirection(cells, warrior);
        if (stepDirection == Direction.NONE) {
            warrior.getGraphic().stop();
//                isMoving = false;
//                battleUnit.getUnit().stop();
            return true;
        } else {
            if (stepDirection != warrior.getDirection()) {
                warrior.getGraphic().stop();
                warrior.setDirection(stepDirection);
                warrior.getGraphic().start(warrior, getAction());
            }
            return false;
        }
    }

}

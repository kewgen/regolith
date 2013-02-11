package com.geargames.regolith.units.map;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.Unit;
import com.geargames.regolith.units.battle.Direction;
import com.geargames.regolith.units.battle.WarriorHelper;

/**
 * User: mkutuzov
 * Date: 01.03.12
 * Задача этого класса - совместить раздельные шаги по клеткам карты и обозрение окружающих ячеек с плавным перемещением
 * бойца по игровому полю.
 */
public class Step {
/*
    private static int speed = ClientConfigurationFactory.getConfiguration().getBattleConfiguration().getWalkSpeed();
    private static double shiftOnTickX = (double) BattleScreen.HORIZONTAL_RADIUS / (double) speed;
    private static double shiftOnTickY = (double) BattleScreen.VERTICAL_RADIUS / (double) speed;
*/
    private BattleScreen screen;
    private Unit unit;
    private Direction step;
    private int ticks;
    private boolean initiated;

    private int mapX;
    private int mapY;
    private int extensionX;
    private int extensionY;

    public Step(){
        initiated = false;
    }

    /**
     * Дёргаем затевая движение.
     */
    public void init() {
        initiated = true;
        ticks = 0;
        extensionX = 0;
        extensionY = 0;
        mapX = unit.getMapX();
        mapY = unit.getMapY();
        if(WarriorHelper.getReachableRadius(unit.getWarrior()) == 0){
            step = Direction.NONE;
        }else{
            step = WarriorHelper.getStepDirection(unit.getWarrior(), screen.getBattle().getMap().getCells());
            unit.getWarrior().setDirection(step);
        }
        unit.getWarrior().setMoving(step != Direction.NONE);
        if(!unit.getWarrior().isMoving() && screen.getUser() == unit){
            ClientBattleHelper.route(unit.getWarrior(), ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
        }
    }

    /**
     * Каждый тик приложения дёргаем это метод.
     * Оно будет дёргаться только если мы затеяли движение и двигаться вообще стоит.
     * Заданное число тиков боец движется от середины одной ячейки к середине следующей,
     * по окончании: мы определяемся со следующей ячейкой и исправляем возможные ошибки, рисуя бойца точно
     * в середине текущей клетки.
     */
    public void onTick() {
        if (initiated && unit.getWarrior().isMoving()) {
            BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
            int speed = battleConfiguration.getWalkSpeed();
            double shiftOnTickX = (double) BattleScreen.HORIZONTAL_RADIUS / (double) speed;
            double shiftOnTickY = (double) BattleScreen.VERTICAL_RADIUS / (double) speed;

            if (speed - ticks >= 0) {
                extensionX += shiftOnTickX * (step.getX() - step.getY());
                extensionY += shiftOnTickY * (step.getY() + step.getX());
                unit.setMapX(extensionX + mapX);
                unit.setMapY(extensionY + mapY);
                ticks++;
            } else {
                unit.setMapX(BattleScreen.HORIZONTAL_RADIUS*(step.getX() - step.getY()) + mapX) ;
                unit.setMapY(BattleScreen.VERTICAL_RADIUS*(step.getY() + step.getX()) + mapY);
                WarriorHelper.step(unit.getWarrior(), step.getX(), step.getY(), battleConfiguration);
                init();
            }
        }
    }

    /**
     * Игровое поле по которому шагает боец.
     *
     * @return
     */
    public BattleScreen getScreen() {
        return screen;
    }

    public void setScreen(BattleScreen screen) {
        this.screen = screen;
    }

    /**
     *  Юнит unit бойца, путь которого должен быть прорисован.
     *
     * @return
     */
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}

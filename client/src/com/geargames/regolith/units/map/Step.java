package com.geargames.regolith.units.map;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.Direction;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 01.03.12
 * Задача этого класса - совместить раздельные шаги по клеткам карты и обозрение окружающих ячеек с плавным перемещением
 * бойца по игровому полю.
 */
public class Step {
    private BattleScreen screen;
    private BattleUnit battleUnit;
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
        mapX = battleUnit.getMapX();
        mapY = battleUnit.getMapY();
        Warrior warrior = battleUnit.getUnit().getWarrior();
        if(WarriorHelper.getReachableRadius(warrior) == 0){
            step = Direction.NONE;
        }else{
            step = WarriorHelper.getStepDirection(warrior, screen.getBattle().getMap().getCells());
            warrior.setDirection(step);
        }
        warrior.setMoving(step != Direction.NONE);
        if(!warrior.isMoving() && screen.getUser() == battleUnit){
            ClientBattleHelper.route(warrior, ClientConfigurationFactory.getConfiguration().getBattleConfiguration());
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
        if (initiated && battleUnit.getUnit().getWarrior().isMoving()) {
            BattleConfiguration battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
            int speed = battleConfiguration.getWalkSpeed();
            double shiftOnTickX = (double) BattleScreen.HORIZONTAL_RADIUS / (double) speed;
            double shiftOnTickY = (double) BattleScreen.VERTICAL_RADIUS / (double) speed;

            if (speed - ticks >= 0) {
                extensionX += shiftOnTickX * (step.getX() - step.getY());
                extensionY += shiftOnTickY * (step.getY() + step.getX());
                battleUnit.setMapX(extensionX + mapX);
                battleUnit.setMapY(extensionY + mapY);
                ticks++;
            } else {
                battleUnit.setMapX(BattleScreen.HORIZONTAL_RADIUS*(step.getX() - step.getY()) + mapX) ;
                battleUnit.setMapY(BattleScreen.VERTICAL_RADIUS*(step.getY() + step.getX()) + mapY);
                WarriorHelper.step(battleUnit.getUnit().getWarrior(), step.getX(), step.getY(), battleConfiguration);
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
     *  Юнит battleUnit бойца, путь которого должен быть прорисован.
     *
     * @return
     */
    public BattleUnit getBattleUnit() {
        return battleUnit;
    }

    public void setBattleUnit(BattleUnit battleUnit) {
        this.battleUnit = battleUnit;
    }
}

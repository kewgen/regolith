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

    private int beginMapX;
    private int beginMapY;
    private int extensionX;
    private int extensionY;

    private int shiftOnTickX;
    private int shiftOnTickY;
    private int speed;
    private Warrior warrior;

    private BattleConfiguration battleConfiguration;

    public Step() {
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
        beginMapX = battleUnit.getMapX();
        beginMapY = battleUnit.getMapY();
        warrior = battleUnit.getUnit().getWarrior();
        speed = warrior.getSpeed();
        shiftOnTickX = BattleScreen.HORIZONTAL_RADIUS / speed;
        shiftOnTickY = BattleScreen.VERTICAL_RADIUS / speed;
        battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();

        boolean isMoving = true;
        if (WarriorHelper.getReachableRadius(warrior) == 0) {
            isMoving = false;
            battleUnit.getUnit().stop();
        } else {
            step = WarriorHelper.getStepDirection(warrior, screen.getBattle().getMap().getCells());
            if (step == Direction.NONE) {
                isMoving = false;
                battleUnit.getUnit().stop();
            } else {
                if (warrior.getDirection() != step || !warrior.isMoving()) {
                    battleUnit.getUnit().stop();
                    warrior.setDirection(step);
                    battleUnit.getUnit().run();
                }
            }
        }

        warrior.setMoving(isMoving);
        if (!isMoving && screen.getUser() == battleUnit) {
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
        if (initiated && warrior.isMoving()) {
            if (speed - ticks > 0) {
                extensionX += shiftOnTickX * (step.getX() - step.getY());
                extensionY += shiftOnTickY * (step.getY() + step.getX());
                battleUnit.setMapX(extensionX + beginMapX);
                battleUnit.setMapY(extensionY + beginMapY);
                ticks++;
            } else {
                battleUnit.setMapX(BattleScreen.HORIZONTAL_RADIUS * (step.getX() - step.getY()) + beginMapX);
                battleUnit.setMapY(BattleScreen.VERTICAL_RADIUS * (step.getY() + step.getX()) + beginMapY);
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
     * Юнит battleUnit бойца, путь которого должен быть прорисован.
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

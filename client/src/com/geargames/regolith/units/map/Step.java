package com.geargames.regolith.units.map;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.Direction;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 01.03.12
 * Задача этого класса - совместить раздельные шаги по клеткам карты и обозрение окружающих ячеек с плавным перемещением
 * бойца по игровому полю.
 */
public abstract class Step implements Tickable {
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
    private BattleMap map;
    private BattleAlliance alliance;

    private BattleConfiguration battleConfiguration;

    public Step() {
        initiated = false;
        battleConfiguration = ClientConfigurationFactory.getConfiguration().getBattleConfiguration();
    }



    /**
     * Дёргаем затевая движение.
     */
    public void init() {
        speed = battleConfiguration.getWalkSpeed();
        shiftOnTickX = BattleScreen.HORIZONTAL_RADIUS / speed;
        shiftOnTickY = BattleScreen.VERTICAL_RADIUS / speed;

        warrior = battleUnit.getUnit().getWarrior();
        alliance = warrior.getBattleGroup().getAlliance();
        map = warrior.getBattleGroup().getAlliance().getBattle().getMap();

        ticks = 0;
        extensionX = 0;
        extensionY = 0;
        beginMapX = battleUnit.getMapX();
        beginMapY = battleUnit.getMapY();

        boolean isMoving = true;
        if (hasToStop(warrior)) {
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
        if (!warrior.isMoving() && screen.getUser() == battleUnit) {
            routeMap(warrior);
        }
        initiated = true;
    }

    protected BattleAlliance getAlliance() {
        return alliance;
    }

    protected BattleConfiguration getBattleConfiguration() {
        return battleConfiguration;
    }

    protected BattleMap getBattleMap(){
        return map;
    }

    /**
     * Отметить стоимость достижимости достижимых точек.
     * @param warrior
     */
    protected abstract void routeMap(Warrior warrior);

    /**
     * Должен ли остановится в начале текущего шага.
     * @param warrior
     * @return
     */
    protected abstract boolean hasToStop(Warrior warrior);

    /**
     * Перевести бойца на следующую клетку карты произведя полное изменение его состояния на сколько это возможно.
     * @param warrior
     * @param stepX
     * @param stepY
     */
    protected abstract void doStepOnMap(Warrior warrior, int stepX, int stepY);

    /**
     * В каждый цикл игрового обновления дёргаем это метод.
     * Оно будет дёргаться только если мы затеяли движение и двигаться вообще стоит.
     * Заданное число тиков боец движется от середины одной ячейки к середине следующей,
     * по окончании: мы определяемся со следующей ячейкой и исправляем возможные ошибки, рисуя бойца точно
     * в середине текущей клетки.
     */
    @Override
    public void onTick() {
        if (initiated && warrior.isMoving()) {
            if (speed - ticks > 1) {
                extensionX += shiftOnTickX * (step.getX() - step.getY());
                extensionY += shiftOnTickY * (step.getY() + step.getX());
                battleUnit.setMapX(extensionX + beginMapX);
                battleUnit.setMapY(extensionY + beginMapY);
                ticks++;
            } else {
                battleUnit.setMapX(BattleScreen.HORIZONTAL_RADIUS * (step.getX() - step.getY()) + beginMapX);
                battleUnit.setMapY(BattleScreen.VERTICAL_RADIUS * (step.getY() + step.getX()) + beginMapY);
                doStepOnMap(warrior, step.getX(), step.getY());
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
     * Юнит, путь которого, должен быть прорисован.
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

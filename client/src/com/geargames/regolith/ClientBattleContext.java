package com.geargames.regolith;

import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.ClientWarriorElement;
import com.geargames.regolith.units.map.DynamicCellElement;

/**
 * User: abarakov
 * Date: 12.05.13
 */
public class ClientBattleContext {
    public static final int GROUND_WIDTH = 348;
    public static final int GROUND_HEIGHT = 174;
    public static final int HORIZONTAL_DIAGONAL = GROUND_WIDTH / 3;
    public static final int VERTICAL_DIAGONAL = GROUND_HEIGHT / 3;
    public static final int SPOT = 10;
    public static final int HORIZONTAL_RADIUS = HORIZONTAL_DIAGONAL / 2;
    public static final int VERTICAL_RADIUS = VERTICAL_DIAGONAL / 2;
    public static final double TANGENT = (VERTICAL_RADIUS + 0.0) / (HORIZONTAL_RADIUS + 0.0);

    private Battle battle;                      // Текущая битва
    private BattleGroup battleGroup;            //
    private BattleAlliance activeAlliance;      // Активный в данный момент боевой альянс, чей сейчас ход
    private ClientWarriorElement activeUnit;    // Активный боец

    private DynamicCellElement selectedElement; // Выбранный на карте динамический элемент
    private short selectedElementCellX;
    private short selectedElementCellY;

    private ClientWarriorCollection groupUnits; // Бойцы текущего клиента
    private ClientWarriorCollection allyUnits;  // Союзные бойцы
    private ClientWarriorCollection enemyUnits; // Вражеские бойцы

    public void initiate(Battle battle) {
        Account account = ClientConfigurationFactory.getConfiguration().getAccount();

        this.battle = battle;
        this.battleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(battle, account.getId());
        this.activeAlliance = null;
        this.activeUnit = null;

        selectedElement = null;
        selectedElementCellX = -1;
        selectedElementCellY = -1;

        groupUnits = ClientBattleHelper.getGroupBattleUnits(battle, account);
        allyUnits = ClientBattleHelper.getAllyBattleUnits(battle, account);
        enemyUnits = ClientBattleHelper.getEnemyBattleUnits(battle, account);
    }

    /**
     * Вернуть объект битва, в которой, в настоящий момент, участвует игрок.
     */
    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }

    /**
     * Моя ли очередь ходить?
     */
    public boolean isMyTurn() {
        return battleGroup.getAlliance() == activeAlliance;
    }

    /**
     * Вернуть активный боевой союз.
     */
    public BattleAlliance getActiveAlliance() {
        return activeAlliance;
    }

    public void setActiveAlliance(BattleAlliance activeAlliance) {
        this.activeAlliance = activeAlliance;
    }

    /**
     * Получить своего активного бойца.
     */
    public ClientWarriorElement getActiveUnit() {
        return activeUnit;
    }

    public void setActiveUnit(ClientWarriorElement activeUnit) {
        this.activeUnit = activeUnit;
    }

    /**
     * Получить элемент на карте, который в данный момент выделен.
     */
    public DynamicCellElement getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(DynamicCellElement selectedElement) {
        this.selectedElement = selectedElement;
    }

    public short getSelectedElementCellX() {
        return selectedElementCellX;
    }

    public void setSelectedElementCellX(short x) {
        selectedElementCellX = x;
    }

    public short getSelectedElementCellY() {
        return selectedElementCellY;
    }

    public void setSelectedElementCellY(short y) {
        selectedElementCellY = y;
    }

    /**
     * Получить список собственных бойцов на карте.
     *
     * @return список объектов класса Warrior.
     */
    public ClientWarriorCollection getGroupUnits() {
        return groupUnits;
    }

    /**
     * Получить список союзных бойцов на карте.
     *
     * @return список объектов класса Warrior.
     */
    public ClientWarriorCollection getAllyUnits() {
        return allyUnits;
    }

    /**
     * Получить список вражеских бойцов на карте.
     *
     * @return список объектов класса Warrior.
     */
    public ClientWarriorCollection getEnemyUnits() {
        return enemyUnits;
    }

}

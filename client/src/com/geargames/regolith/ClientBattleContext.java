package com.geargames.regolith;

import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.ClientWarriorElement;

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

    private ClientWarriorCollection groupUnits; // бойцы текущего клиента
    private ClientWarriorCollection allyUnits;  // союзные бойцы
    private ClientWarriorCollection enemyUnits; // вражеские бойцы

    private Battle battle;                      // Текущая битва
    private BattleGroup battleGroup;            //
    private BattleAlliance activeAlliance;      // Активный в данный момент боевой альянс, чей сейчас ход
    private ClientWarriorElement activeUnit;    // Активный боец

    public void initiate(Battle battle) {
        Account account = ClientConfigurationFactory.getConfiguration().getAccount();

        this.battle = battle;
        this.battleGroup = ClientBattleHelper.tryFindBattleGroupByAccountId(battle, account.getId());
        this.activeAlliance = null;
        this.activeUnit = null;

        groupUnits = ClientBattleHelper.getGroupBattleUnits(battle, account);
        allyUnits = ClientBattleHelper.getAllyBattleUnits(battle, account);
        enemyUnits = ClientBattleHelper.getEnemyBattleUnits(battle, account);
    }

    /**
     * Вернуть объект битва отрисовка которой происходит с помощью экрана.
     *
     * @return
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

    public void setBattleGroup(BattleGroup battleGroup) {
        this.battleGroup = battleGroup;
    }

    /**
     * Моя ли очередь ходить?
     *
     * @return
     */
    public boolean isMyTurn() {
        return battleGroup.getAlliance() == activeAlliance;
    }

    /**
     * Вернуть активный боевой союз.
     *
     * @return
     */
    public BattleAlliance getActiveAlliance() {
        return activeAlliance;
    }

    public void setActiveAlliance(BattleAlliance activeAlliance) {
        this.activeAlliance = activeAlliance;
    }

    /**
     * Вернуть своего активного бойца.
     *
     * @return
     */
    public ClientWarriorElement getActiveUnit() {
        return activeUnit;
    }

    public void setActiveUnit(ClientWarriorElement activeUnit) {
        this.activeUnit = activeUnit;
    }

    public ClientWarriorCollection getGroupUnits() {
        return groupUnits;
    }

    public ClientWarriorCollection getAllyUnits() {
        return allyUnits;
    }

    public ClientWarriorCollection getEnemyUnits() {
        return enemyUnits;
    }

    public void setGroupUnits(ClientWarriorCollection list) {
        groupUnits = list;
    }

    public void setAllyUnits(ClientWarriorCollection list) {
        allyUnits = list;
    }

    public void setEnemyUnits(ClientWarriorCollection list) {
        enemyUnits = list;
    }

}

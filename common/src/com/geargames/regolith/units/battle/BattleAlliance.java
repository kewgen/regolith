package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.ExitZone;
import com.geargames.regolith.units.Entity;

/**
 * Военный союз представляет собой совокупность боевых групп (клиентов) участвующих в битве (одна из команд игроков).
 * User: mkutuzov
 * Date: 07.03.12
 */
public class BattleAlliance extends Entity {
    private BattleGroupCollection allies;
    private Battle battle;
    private byte number;
    private ExitZone exit;

    /**
     * Зона выхода любого из бойцов военного союза.
     * @return
     */
    public ExitZone getExit() {
        return exit;
    }

    public void setExit(ExitZone exit) {
        this.exit = exit;
    }

    /**
     * Номер боевого союза.
     * @return
     */
    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    /**
     * Отряды бойцов (пользовательских приложений) участвующих в союзе.
     * @return
     */
    public BattleGroupCollection getAllies() {
        return allies;
    }

    public void setAllies(BattleGroupCollection allies) {
        this.allies = allies;
    }

    /**
     * Битва.
     * @return
     */
    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }
}

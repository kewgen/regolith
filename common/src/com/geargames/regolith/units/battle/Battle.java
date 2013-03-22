package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.Entity;

/**
 * Битва.
 * User: mkutuzov
 * Date: 20.02.12
 */
public class Battle extends Entity {
    private String name;
    private BattleMap map;
    private BattleAlliance[] alliances;
    private Account author;
    private BattleType battleType;

    public BattleType getBattleType() {
        return battleType;
    }

    public void setBattleType(BattleType battleType) {
        this.battleType = battleType;
    }

    /**
     * Вернет ссылку на игрока (его учетную запись) создавшего бой.
     * @return
     */
    public Account getAuthor() {
        return author;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    /**
     * Вернет список боевых союзов, которые принимают участие в битве.
     * @return
     */
    public BattleAlliance[] getAlliances() {
        return alliances;
    }

    public void setAlliances(BattleAlliance[] alliances) {
        this.alliances = alliances;
    }

    /**
     * Название битвы.
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Карта битвы.
     * @return
     */
    public BattleMap getMap() {
        return map;
    }

    public void setMap(BattleMap map) {
        this.map = map;
    }

}

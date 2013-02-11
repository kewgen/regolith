package com.geargames.regolith.units.battle;

/**
 * User: mikhail v. kutuzov
 * Date: 08.10.12
 * Time: 21:39
 */
public abstract class ServerBattleType extends BattleType {
    /**
     * Проверка: следует ли начинать битву?
     * @param battle
     * @return
     */
    public abstract boolean haveToStart(Battle battle);

    /**
     * Проверка: следует ли заканчивать битву?
     * @param battle
     * @return
     */
    public abstract boolean haveToFinish(Battle battle);

    /**
     * Вернуть альянс-победитель.
     * @return
     */
    public abstract BattleAlliance getWinner(Battle battle);
}

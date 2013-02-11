package com.geargames.regolith.map.router;

import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 21.02.12
 */
public abstract class Router {
    /**
     * Проставим стоимости достижимости(в ходах) точек карты находящихся в пределах досягаемости
     * бойца warrior
     * Стоимость достижимости - наименьшее количество клеток пройдя через которые боец достигнет этой клетки.
     * @param warrior
     */
    public abstract void route(Warrior warrior);
}

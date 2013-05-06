package com.geargames.regolith.map.router;

import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.units.map.HumanElement;

/**
 * User: mkutuzov
 * Date: 21.02.12
 */
public abstract class Router {

    /**
     * Проставим стоимости достижимости (в ходах) точек карты находящихся в пределах досягаемости бойца unit
     * Стоимость достижимости - наименьшее количество клеток, пройдя через которые, боец достигнет этой клетки.
     *
     * @param unit
     */
    public abstract void route(HumanElement unit, BattleConfiguration battleConfiguration);

}

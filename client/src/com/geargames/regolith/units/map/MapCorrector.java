package com.geargames.regolith.units.map;

import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.BattleScreen;

/**
 * Users: mkutuzov, abarakov
 * Date: 15.02.12
 */
public abstract class MapCorrector {

    /**
     * Изменяет координаты левого верхнего угла экрана на карте, чтобы те не выходили за допустимые пределы.
     *
     * @param x координата левого верхнего угла
     * @param y координата левого верхнего угла
     * @return пара координат, лежащих в допустимых пределах.
     */
    public abstract Pair correct(int x, int y, BattleScreen battleScreen);

}

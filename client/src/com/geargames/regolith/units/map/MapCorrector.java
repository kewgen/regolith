package com.geargames.regolith.units.map;

/**
 * User: mkutuzov
 * Date: 15.02.12
 */
public abstract class MapCorrector {
    /**
     * Изменяет координаты левого верхнего угла экрана на карте.
     * @param x координата левого верхнего угла
     * @param y координата левого верхнего угла
     */
    public abstract void correct(int x, int y, BattleScreen battleScreen);
}

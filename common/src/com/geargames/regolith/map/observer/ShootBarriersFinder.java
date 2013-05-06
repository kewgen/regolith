package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.map.HumanElement;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public class ShootBarriersFinder extends BattleCellMaintainer {
    private Pair coordinates;

    public ShootBarriersFinder() {
        coordinates = new Pair();
    }

    /**
     * Возвращает true если оружием бойца warrior можно прострелить эту клетку, иначе - false.
     * Дёргаясь для каждой ячейки cells[x][y] оно складывает последнюю найденную преграду в пол роста
     * во внутреннюю переменную класса.
     *
     * @param cells
     * @param unit
     * @param toDo
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean maintain(BattleCell[][] cells, HumanElement unit, boolean toDo, int x, int y) {
        CellElement element = cells[x][y].getElement();
        if (element != null && toDo) {
            WeaponCategory category = unit.getHuman().getWeapon().getWeaponType().getCategory();
            if (!element.isAbleToShootThrough(category)) {
                coordinates.setX(x);
                coordinates.setY(y);
                if (!element.isHalfLong()) {
                    return false;
                }
            }
        }
        return toDo;
    }

    public Pair getCoordinates() {
        return coordinates;
    }

}

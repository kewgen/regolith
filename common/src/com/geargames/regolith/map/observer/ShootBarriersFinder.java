package com.geargames.regolith.map.observer;

import com.geargames.regolith.map.PairAndElement;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.CellElement;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 15.03.12
 */
public class ShootBarriersFinder extends BattleCellMaintainer {
    private PairAndElement coordinates;

    public ShootBarriersFinder() {
        coordinates = new PairAndElement();
    }

    /**
     * Возвращает true если оружием бойца warrior можно прострелить эту клетку, иначе - false.
     * Дёргаясь для каждой ячейки cells[x][y] оно складывает последнюю найденную преграду в пол роста
     * во внутреннюю переменную класса.
     *
     * @param cells
     * @param warrior
     * @param toDo
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean maintain(BattleCell[][] cells, Warrior warrior, boolean toDo, int x, int y) {
        CellElement[] elements = cells[x][y].getElements();
        int length = cells[x][y].getSize();

        if (toDo) {
            for (int i = length - 1; i >= 0; i--) {
                CellElement element = elements[i];

                WeaponCategory category = warrior.getWeapon().getWeaponType().getCategory();
                if (!element.isAbleToShootThrough(category)) {
                    coordinates.setX(x);
                    coordinates.setY(y);
                    coordinates.setElement(element);
                    if (!element.isHalfLong()) {
                        return false;
                    }
                }
            }
        }
        return toDo;
    }

    public PairAndElement getCoordinates() {
        return coordinates;
    }

}

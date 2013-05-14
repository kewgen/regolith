package com.geargames.regolith.helpers;

import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.CellElement;

/**
 * User: mvkutuzov
 * Date: 14.05.13
 * Time: 9:29
 */
public class BattleCellHelper {
    /**
     * Есть ли что либо над или в этом слое.
     * @param cell
     * @param layer
     * @return
     */
    public static boolean isAnythingUpperOrEqualPresented(BattleCell cell, byte layer) {
        CellElement[] elements = cell.getElements();
        int size = cell.getSize();
        for (int i = 0; i < size; i++) {
            if (elements[i].getLayer() >= layer) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получить первый объект из заданного слоя ячейки cell.
     * @param cell
     * @param layer
     * @return  null если слой отсутсвует.
     */
    public static CellElement getElementFromLayer(BattleCell cell, byte layer) {
        CellElement[] elements = cell.getElements();
        int size = cell.getSize();
        for (int i = size - 1; i >= 0; i--) {
            CellElement element = elements[i];
            if (element.getLayer() == layer) {
                return element;
            }
        }
        return null;
    }

}

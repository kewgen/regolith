package com.geargames.regolith.units.map;

import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.battle.BattleAlliance;

import java.io.Serializable;

/**
 * Абстрактная клетка игрового поля, должна расширяться клиентской и серверной версиями.
 * Users: mkutuzov, abarakov
 * Date: 22.03.12
 */
public abstract class BattleCell implements Serializable {
    /**
     * Какие элементы могут располагаться в ячейке:
     *   static   very bottom  подложка
     *   static   bottom       подсветка зоны, подсветка препятствий
     *   static   middle       препятствия
     *   dynamic  middle       выброшенные предметы
     *   dynamic  top          бойцы
     *   static   top          значки (высота препятствия и др.)
     *   static   very top     туман войны
     */

    private CellElement[] elements;
    private byte size;
    private byte order;

    public BattleCell() {
        elements = new CellElement[3];
        size = 0;
    }

    /**
     * Вернуть массив элементов находящихся в этой ячейке карты.
     *
     * @return
     */
    public CellElement[] getElements() {
        return elements;
    }

    public byte getSize() {
        return size;
    }

    @Deprecated
    public CellElement getElement() {
        if (size == 0) {
            return null;
        } else {
            return elements[size - 1];
        }
    }

    /**
     * Добавить элемент в ячейку в соответствующий слой.
     *
     * @param element
     */
    public void addElement(CellElement element) {
        int insertIndex = 0;
        if (size > 0) {
            int capacity = elements.length;
            if (size == capacity) {
                CellElement[] oldElements = elements;
                elements = new CellElement[capacity + 2];
                //todo: Реализовать одновременное копирование массива и вставку нового элемента
                for (int i = 0; i < capacity; i++) {
                    elements[i] = oldElements[i];
                }
            }
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i].getLayer() > element.getLayer()) {
                    elements[i + 1] = elements[i];
                } else {
                    insertIndex = i + 1;
                    break;
                }
            }
        }
        elements[insertIndex] = element;
        size++;
    }

    /**
     * Удалить элемент из ячейки.
     *
     * @param element
     */
    public void removeElement(CellElement element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == element) {
                for (int j = i; j < size - 1; j++) {
                    elements[j] = elements[j + 1];
                }
                size--;
                break;
            }
        }
    }

    /**
     * Вернуть битовую маску достижимости ячейки карты.
     * 0-6 биты содержат наименьшее количество клеток (до 64 клеток), которое надо будет преодолеть бойцу, чтобы
     * добраться до этой точки.
     *
     * @return
     */
    public byte getOrder() {
        return order;
    }

    public void setOrder(byte order) {
        this.order = order;
    }

    /**
     * Вернуть битовую маску видимости клеток бойцами военного союза battleAlliance.
     * Если клетка видима бойцом с номером N в военном союзе, то бит карты установлен.
     *
     * @param battleAlliance
     * @return
     */
    public abstract short getVisibility(BattleAlliance battleAlliance);

    /**
     * Вернуть битовую маску оптимального пути бойцов военного союза battleAlliance.
     * Если клетка принадлежит оптимальному пути.
     *
     * @param battleAlliance
     * @return
     */
    public abstract short getOptimalPath(BattleAlliance battleAlliance);

    /**
     * Была ли точка засвечена бойцами военного союза battleAlliance.
     *
     * @param battleAlliance
     * @return
     */
    public abstract boolean isVisited(BattleAlliance battleAlliance);

    public abstract void setVisibility(BattleAlliance battleAlliance, short visibility);

    public abstract void setOptimalPath(BattleAlliance battleAlliance, short path);

    public abstract void setVisited(BattleAlliance battleAlliance, boolean visited);

}

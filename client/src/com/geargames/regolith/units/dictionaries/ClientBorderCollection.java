package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Border;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ClientBorderCollection extends BorderCollection {
    private Vector borders;

    public Vector getBorders() {
        return borders;
    }

    public void setBorders(Vector borders) {
        this.borders = borders;
    }

    public Border get(int index) {
        return (Border)borders.elementAt(index);
    }

    public void add(Border border) {
        borders.addElement(border);
    }

    public void insert(Border border, int index) {
        borders.insertElementAt(border, index);
    }

    public void remove(int index) {
        borders.remove(index);
    }

    public int size() {
        return borders.size();
    }

    public void set(Border border, int index) {
        borders.setElementAt(border, index);
    }
}

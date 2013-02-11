package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Border;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ServerBorderCollection extends BorderCollection {

    private List<Border> borders;

    public List<Border> getBorders() {
        return borders;
    }

    public void setBorders(List<Border> borders) {
        this.borders = borders;
    }

    public Border get(int index) {
        return borders.get(index);
    }

    public void add(Border border) {
        borders.add(border);
    }

    public void insert(Border border, int index) {
        borders.add(index, border);
    }

    public void remove(int index) {
        borders.remove(index);
    }

    public int size() {
        return borders.size();
    }

    public void set(Border border, int index) {
        borders.set(index, border);
    }
}

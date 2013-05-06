package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.map.HumanElement;

import java.util.List;

/**
 * User: abarakov
 * Date: 30.04.13
 */
public class ServerHumanElementCollection extends HumanElementCollection {
    private List<HumanElement> elements;

    public List<HumanElement> getElements() {
        return elements;
    }

    public void setElements(List<HumanElement> elements) {
        this.elements = elements;
    }

    @Override
    public HumanElement get(int index) {
        return elements.get(index);
    }

    @Override
    public void add(HumanElement item) {
        elements.add(item);
    }

    @Override
    public void addAll(HumanElementCollection collection) {
        elements.addAll(((ServerHumanElementCollection) collection).elements);
    }

    @Override
    public void insert(HumanElement item, int index) {
        elements.add(index, item);
    }

    @Override
    public void set(HumanElement item, int index) {
        elements.set(index, item);
    }

    @Override
    public void remove(int index) {
        elements.remove(index);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public void clear() {
        elements.clear();
    }

}

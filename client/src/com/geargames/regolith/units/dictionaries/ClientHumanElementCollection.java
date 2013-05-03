package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.map.HumanElement;

import java.util.Vector;

/**
 * User: abarakov
 * Date: 30.04.13
 */
public class ClientHumanElementCollection extends HumanElementCollection {
    private Vector elements;

    public Vector getElements() {
        return elements;
    }

    public void setElements(Vector elements) {
        this.elements = elements;
    }

    @Override
    public HumanElement get(int index) {
        return (HumanElement) elements.elementAt(index);
    }

    @Override
    public void add(HumanElement item) {
        elements.addElement(item);
    }

    @Override
    public void addAll(HumanElementCollection collection) {
        elements.ensureCapacity(elements.size() + collection.size());
        for (int i = 0; i < collection.size(); i++) {
            elements.addElement(collection.get(i));
        }
    }

    @Override
    public void insert(HumanElement item, int index) {
        elements.insertElementAt(item, index);
    }

    @Override
    public void set(HumanElement item, int index) {
        elements.setElementAt(item, index);
    }

    @Override
    public void remove(int index) {
        elements.removeElementAt(index);
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

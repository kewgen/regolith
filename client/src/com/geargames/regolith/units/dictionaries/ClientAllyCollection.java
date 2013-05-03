package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Ally;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Date: 22.08.12
 * Time: 15:32
 */
public class ClientAllyCollection extends AllyCollection {
    private Vector allies;

    public Vector getAllies() {
        return allies;
    }

    public void setAllies(Vector allies) {
        this.allies = allies;
    }

    public Ally get(int index) {
        return (Ally) allies.elementAt(index);
    }

    public void add(Ally ally) {
        allies.addElement(ally);
    }

    public void insert(Ally ally, int index) {
        allies.insertElementAt(ally, index);
    }

    public void set(Ally ally, int index) {
        allies.setElementAt(ally, index);
    }

    public void remove(int index) {
        allies.removeElementAt(index);
    }

    public int size() {
        return allies.size();
    }

    @Override
    public void clear() {
        allies.clear();
    }

}

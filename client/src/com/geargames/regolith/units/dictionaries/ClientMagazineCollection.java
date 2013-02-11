package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Magazine;

import java.util.Vector;

/**
 * @author Mikhail_Kutuzov
 *         created: 29.05.12  23:53
 */
public class ClientMagazineCollection extends MagazineCollection {
    private Vector magazines;

    public Vector getMagazines() {
        return magazines;
    }

    public void setMagazines(Vector magazines) {
        this.magazines = magazines;
    }

    public Magazine get(int index) {
        return (Magazine)magazines.elementAt(index);
    }

    public void add(Magazine magazine) {
        magazines.addElement(magazine);
    }

    public void insert(Magazine magazine, int index) {
        magazines.insertElementAt(magazine, index);
    }

    public void set(Magazine magazine, int index) {
        magazines.set(index, magazine);
    }

    public void remove(int index) {
        magazines.removeElementAt(index);
    }

    public int size() {
        return magazines.size();
    }
}

package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Warrior;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 02.05.12
 */
public class ClientWarriorCollection extends WarriorCollection {
    private Vector warriors;

    public ClientWarriorCollection() {
    }

    public ClientWarriorCollection(Vector vector) {
        warriors = vector;
    }

    public Vector getWarriors() {
        return warriors;
    }

    public void setWarriors(Vector warriors) {
        this.warriors = warriors;
    }

    public Warrior get(int index) {
        return (Warrior) warriors.elementAt(index);
    }

    public void add(Warrior warrior) {
        warriors.addElement(warrior);
    }

    public void insert(Warrior warrior, int index) {
        warriors.insertElementAt(warrior, index);
    }

    public void set(Warrior warrior, int index) {
        warriors.setElementAt(warrior, index);
    }

    public void remove(int index) {
        warriors.removeElementAt(index);
    }

    public int size() {
        return warriors.size();
    }

    public boolean contains(Warrior warrior) {
        int size = size();
        for (int i = 0; i < size; i++) {
            if (warrior == get(i)) {
                return true;
            }
        }
        return false;
    }
}

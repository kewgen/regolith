package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Human;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Date: 23.08.12
 * Time: 11:37
 */
public class ClientHumanCollection extends HumanCollection {
    private Vector humans;

    public ClientHumanCollection(Vector humans) {
        this.humans = humans;
    }

    public Human get(int index) {
        return (Human)humans.elementAt(index);
    }

    public void add(Human human) {
        humans.add(human);
    }

    public void insert(Human human, int index) {
        humans.insertElementAt(human, index);
    }

    public void set(Human human, int index) {
        humans.setElementAt(human, index);
    }

    public void clear() {
        humans.clear();
    }

    public void remove(int index) {
        humans.removeElementAt(index);
    }

    public int size() {
        return humans.size();
    }
}

package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Barrier;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ClientBarrierCollection extends BarrierCollection {
    private Vector barriers;

    public Vector getBarriers() {
        return barriers;
    }

    public void setBarriers(Vector barriers) {
        this.barriers = barriers;
    }

    public Barrier get(int index) {
        return (Barrier) barriers.elementAt(index);
    }

    public void add(Barrier barrier) {
        barriers.addElement(barrier);
    }

    public void insert(Barrier barrier, int index) {
        barriers.insertElementAt(barrier, index);
    }

    public void remove(int index) {
        barriers.remove(index);
    }

    public int size() {
        return barriers.size();
    }

    public void set(Barrier barrier, int index) {
        barriers.setElementAt(barrier, index);
    }

}

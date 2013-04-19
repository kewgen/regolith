package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Barrier;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ServerBarrierCollection extends BarrierCollection {

    private List<Barrier> barriers;

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(List<Barrier> barriers) {
        this.barriers = barriers;
    }

    public Barrier get(int index) {
        return barriers.get(index);
    }

    public void add(Barrier barrier) {
        barriers.add(barrier);
    }

    public void insert(Barrier barrier, int index) {
        barriers.add(index, barrier);
    }

    public void remove(int index) {
        barriers.remove(index);
    }

    public int size() {
        return barriers.size();
    }

    public void set(Barrier barrier, int index) {
        barriers.set(index, barrier);
    }

}

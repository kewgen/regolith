package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.AbstractTackle;
import com.geargames.regolith.units.tackle.StateTackle;

import java.util.List;

/**
 * @author Mikhail_Kutuzov
 *         created: 29.05.12  23:26
 */
public class ServerStateTackleCollection extends StateTackleCollection {
    private List<StateTackle> tackles;

    public ServerStateTackleCollection() {
    }

    public ServerStateTackleCollection(List<StateTackle> tackles) {
        this.tackles = tackles;
    }

    public List<StateTackle> getTackles() {
        return tackles;
    }

    public void setTackles(List<StateTackle> tackles) {
        this.tackles = tackles;
    }

    @Override
    public StateTackle get(int index) {
        return tackles.get(index);
    }

    @Override
    public void add(StateTackle abstractTackle) {
        tackles.add(abstractTackle);
    }

    @Override
    public void insert(StateTackle abstractTackle, int index) {
        tackles.add(index, abstractTackle);
    }

    @Override
    public void set(StateTackle abstractTackle, int index) {
        tackles.set(index, abstractTackle);
    }

    @Override
    public void remove(int index) {
        tackles.remove(index);
    }

    @Override
    public int size() {
        return tackles.size();
    }
}

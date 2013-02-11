package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Rank;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public class ClientRankCollection extends RankCollection {

    private Vector ranks;

    public Vector getRanks() {
        return ranks;
    }

    public void setRanks(Vector ranks) {
        this.ranks = ranks;
    }

    public Rank get(int index) {
        return (Rank)ranks.elementAt(index);
    }

    public void add(Rank rank) {
        ranks.addElement(rank);
    }

    public void insert(Rank rank, int index) {
        ranks.insertElementAt(rank, index);
    }

    public void remove(int index) {
        ranks.removeElementAt(index);
    }

    public int size() {
        return ranks.size();
    }

    public void set(Rank rank, int index) {
        ranks.setElementAt(rank, index);
    }
}

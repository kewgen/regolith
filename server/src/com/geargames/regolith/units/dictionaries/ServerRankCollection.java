package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Rank;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public class ServerRankCollection extends RankCollection {
    private List<Rank> ranks;

    public List<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }

    public Rank get(int index) {
        return ranks.get(index);
    }

    public void add(Rank rank) {
        ranks.add(rank);
    }

    public void insert(Rank rank, int index) {
        ranks.set(index, rank);
    }

    public void remove(int index) {
        ranks.remove(index);
    }

    public int size() {
        return ranks.size();
    }

    public void set(Rank rank, int index) {
        ranks.set(index, rank);
    }
}

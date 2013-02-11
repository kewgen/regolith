package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Ally;

import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 22.08.12
 * Time: 15:36
 */
public class ServerAllyCollection extends AllyCollection {
    private List<Ally> allies;

    public List<Ally> getAllies() {
        return allies;
    }

    public void setAllies(List<Ally> allies) {
        this.allies = allies;
    }

    @Override
    public Ally get(int index) {
        return allies.get(index);
    }

    @Override
    public void add(Ally ally) {
        allies.add(ally);
    }

    @Override
    public void insert(Ally ally, int index) {
        allies.add(index, ally);
    }

    @Override
    public void set(Ally ally, int index) {
        allies.set(index, ally);
    }

    @Override
    public void remove(int index) {
        allies.remove(index);
    }

    @Override
    public int size() {
        return allies.size();
    }

    @Override
    public void clear() {
        allies.clear();
    }
}

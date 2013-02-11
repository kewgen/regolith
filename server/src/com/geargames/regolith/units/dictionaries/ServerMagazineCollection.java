package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Magazine;

import java.util.List;

/**
 * @author Mikhail_Kutuzov
 *         created: 29.05.12  23:47
 */
public class ServerMagazineCollection extends MagazineCollection {
    private List<Magazine> magazines;

    public List<Magazine> getMagazines() {
        return magazines;
    }

    public void setMagazines(List<Magazine> magazines) {
        this.magazines = magazines;
    }

    @Override
    public Magazine get(int index) {
        return magazines.get(index);
    }

    @Override
    public void add(Magazine magazine) {
        magazines.add(magazine);
    }

    @Override
    public void insert(Magazine magazine, int index) {
        magazines.add(index,magazine);
    }

    @Override
    public void set(Magazine magazine, int index) {
        magazines.set(index, magazine);
    }

    @Override
    public void remove(int index) {
        magazines.remove(index);
    }

    @Override
    public int size() {
        return magazines.size();
    }
}

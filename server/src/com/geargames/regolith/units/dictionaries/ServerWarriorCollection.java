package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Warrior;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 02.05.12
 */
public class ServerWarriorCollection extends WarriorCollection {
    private List<Warrior> warriors;

    public ServerWarriorCollection(List<Warrior> warriors) {
        this.warriors = warriors;
    }

    public ServerWarriorCollection() {
    }

    public List<Warrior> getWarriors() {
        return warriors;
    }

    public void setWarriors(List<Warrior> warriors) {
        this.warriors = warriors;
    }

    public Warrior get(int index) {
        return warriors.get(index);
    }

    public void add(Warrior warrior) {
        warriors.add(warrior);
    }

    @Override
    public void addAll(WarriorCollection collection) {
        warriors.addAll(((ServerWarriorCollection) collection).warriors);
    }

    public void insert(Warrior warrior, int index) {
        warriors.add(index, warrior);
    }

    public void set(Warrior warrior, int index) {
        warriors.set(index, warrior);
    }

    @Override
    public boolean contains(Warrior warrior) {
        return warriors.contains(warrior);
    }

    public void remove(int index) {
        warriors.remove(index);
    }

    public int size() {
        return warriors.size();
    }

    @Override
    public void clear() {
        warriors.clear();
    }

}

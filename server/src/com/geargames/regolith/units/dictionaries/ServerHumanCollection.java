package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Human;

import java.util.List;

/**
 * User: mikhail v. kutuzov
 * Date: 23.08.12
 * Time: 11:40
 */
public class ServerHumanCollection extends HumanCollection {
    private List<Human> humans;

    public ServerHumanCollection(List<Human> humans) {
        this.humans = humans;
    }

    @Override
    public Human get(int index) {
        return humans.get(index);
    }

    @Override
    public void add(Human human) {
        humans.add(human);
    }

    @Override
    public void insert(Human human, int index) {
        humans.add(index, human);
    }

    @Override
    public void set(Human human, int index) {
        humans.set(index, human);
    }

    @Override
    public void clear() {
        humans.clear();
    }

    @Override
    public void remove(int index) {
        humans.remove(index);
    }

    @Override
    public int size() {
        return humans.size();
    }
}

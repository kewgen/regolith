package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Battle;

import java.util.List;

/**
 * User: mikhail v. kutuzov
 */
public class ServerBattleCollection extends BattleCollection {
    private List<Battle> battles;

    public List<Battle> getBattles() {
        return battles;
    }

    public void setBattles(List<Battle> battles) {
        this.battles = battles;
    }

    @Override
    public Battle get(int index) {
        return battles.get(index);
    }

    @Override
    public void add(Battle battle) {
        battles.add(battle);
    }

    @Override
    public void insert(Battle battle, int index) {
        battles.add(index, battle);
    }

    @Override
    public void set(Battle battle, int index) {
        battles.set(index, battle);
    }

    @Override
    public void remove(int index) {
        battles.remove(index);
    }

    @Override
    public int size() {
        return battles.size();
    }
}

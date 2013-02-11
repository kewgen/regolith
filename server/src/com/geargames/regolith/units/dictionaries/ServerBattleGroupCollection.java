package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.BattleGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * User: mkutuzov
 * Date: 12.05.12
 */
public class ServerBattleGroupCollection extends BattleGroupCollection {
    private List<BattleGroup> battleGroups;

    public ServerBattleGroupCollection(){
        battleGroups = new LinkedList<BattleGroup>();
    }

    public List<BattleGroup> getBattleGroups() {
        return battleGroups;
    }

    public void setBattleGroups(List<BattleGroup> battleGroups) {
        this.battleGroups = battleGroups;
    }

    public BattleGroup get(int index) {
        return battleGroups.get(index);
    }

    public void add(BattleGroup battleGroup) {
        battleGroups.add(battleGroup);
    }

    public void insert(BattleGroup battleGroup, int index) {
        battleGroups.add(index, battleGroup);
    }

    public void set(BattleGroup battleGroup, int index) {
        battleGroups.set(index, battleGroup);
    }

    public void remove(int index) {
        battleGroups.remove(index);
    }

    public int size() {
        return battleGroups.size();
    }
}

package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.BattleGroup;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 12.05.12
 */
public class ClientBattleGroupCollection extends BattleGroupCollection {
    private Vector battleGroups;

    public ClientBattleGroupCollection(Vector battleGroups) {
        this.battleGroups = battleGroups;
    }

    public ClientBattleGroupCollection() {
        this.battleGroups = new Vector();
    }

    public BattleGroup get(int index) {
        return (BattleGroup)battleGroups.elementAt(index);
    }

    public void add(BattleGroup battleGroup) {
        battleGroups.addElement(battleGroup);
    }

    public void insert(BattleGroup battleGroup, int index) {
        battleGroups.insertElementAt(battleGroup, index);
    }

    public void set(BattleGroup battleGroup, int index) {
        battleGroups.setElementAt(battleGroup, index);
    }

    public void remove(int index) {
        battleGroups.removeElementAt(index);
    }

    public int size() {
        return battleGroups.size();
    }
}

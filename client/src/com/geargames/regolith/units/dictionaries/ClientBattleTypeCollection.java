package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.BattleType;

import java.util.Vector;

/**
 * @author Mikhail_Kutuzov
 *         created: 11.05.12  12:33
 */
public class ClientBattleTypeCollection extends BattleTypeCollection{
    private Vector battleTypes;

    public Vector getBattleTypes() {
        return battleTypes;
    }

    public void setBattleTypes(Vector battleTypes) {
        this.battleTypes = battleTypes;
    }

    public BattleType get(int index) {
        return (BattleType)battleTypes.elementAt(index);
    }

    public void add(BattleType battleType) {
            battleTypes.addElement(battleType);
    }

    public void insert(BattleType battleType, int index) {
        battleTypes.insertElementAt(battleType, index);
    }

    public void set(BattleType battleType, int index) {
        battleTypes.setElementAt(battleType, index);
    }

    public void remove(int index) {
        battleTypes.removeElementAt(index);
    }

    public int size() {
        return battleTypes.size();
    }
}

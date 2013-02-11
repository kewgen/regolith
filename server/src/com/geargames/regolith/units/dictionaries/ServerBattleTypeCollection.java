package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.BattleType;

import java.util.List;

/**
 * @author Mikhail_Kutuzov
 *         created: 11.05.12  12:41
 */
public class ServerBattleTypeCollection extends BattleTypeCollection {
    private List<BattleType> battleTypes;

    public ServerBattleTypeCollection() {
    }

    public ServerBattleTypeCollection(List<BattleType> battleTypes) {
        this.battleTypes = battleTypes;
    }

    public List<BattleType> getBattleTypes() {
        return battleTypes;
    }

    public void setBattleTypes(List<BattleType> battleTypes) {
        this.battleTypes = battleTypes;
    }

    public BattleType get(int index) {
        return battleTypes.get(index);
    }

    public void add(BattleType battleType) {
        battleTypes.add(battleType);
    }

    public void insert(BattleType battleType, int index) {
        battleTypes.add(index, battleType);
    }

    public void set(BattleType battleType, int index) {
        battleTypes.set(index, battleType);
    }

    public void remove(int index) {
        battleTypes.remove(index);
    }

    public int size() {
        return battleTypes.size();
    }
}

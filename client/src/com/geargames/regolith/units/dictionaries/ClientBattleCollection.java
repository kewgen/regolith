package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.battle.Battle;
import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 *
 */
public class ClientBattleCollection extends BattleCollection {
    private Vector battles;

    public Vector getBattles() {
        return battles;
    }

    public void setBattles(Vector battles) {
        this.battles = battles;
    }

    public Battle get(int index) {
        return (Battle)battles.elementAt(index);
    }

    public void add(Battle battle) {
        battles.addElement(battle);
    }

    public void insert(Battle battle, int index) {
        battles.insertElementAt(battle, index);
    }

    public void set(Battle battle, int index) {
        battles.setElementAt(battle, index);
    }

    public void remove(int index) {
        battles.removeElementAt(index);
    }

    public int size() {
        return battles.size();
    }
}

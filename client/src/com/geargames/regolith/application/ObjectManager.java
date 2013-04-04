package com.geargames.regolith.application;

import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Класс управляет сущностями клиента. Сделан для того, чтоб минимизировать затраты на сборку мусора.
 */
public class ObjectManager {
    private static ObjectManager instance;

    private Battle clientBattle;
    private ClientBattleCollection battleCollection;

    private ObjectManager() {
        battleCollection = new ClientBattleCollection();
        battleCollection.setBattles(new Vector());
        clientBattle = new Battle();
    }

    public ClientBattleCollection getBattleCollection() {
        return battleCollection;
    }

    public Battle getClientBattle() {
        return clientBattle;
    }

    public static ObjectManager getInstance() {
        if (instance == null) {
            instance = new ObjectManager();
        }
        return instance;
    }

    public Battle findBattleById(int id) {
        for (int i = 0; i < battleCollection.size(); i++) {
            Battle battle = battleCollection.get(i);
            if (battle.getId() == id) {
                return battle;
            }
        }
        return null;
    }

    public Battle removeBattleById(int id) {
        for (int i = 0; i < battleCollection.size(); i++) {
            Battle battle = battleCollection.get(i);
            if (battle.getId() == id) {
                battleCollection.remove(i);
                return battle;
            }
        }
        return null;
    }

}

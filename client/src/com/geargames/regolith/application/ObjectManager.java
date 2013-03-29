package com.geargames.regolith.application;

import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.BattleCollection;
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
        BattleCollection collection = ObjectManager.getInstance().getBattleCollection();
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getId() == id) {
                return collection.get(i);
            }
        }
        return null;
    }

    public Battle removeBattleById(int id) {
        BattleCollection collection = ObjectManager.getInstance().getBattleCollection();
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getId() == id) {
                Battle battle = collection.get(i);
                collection.remove(i);
                return battle;
            }
        }
        return null;
    }

}

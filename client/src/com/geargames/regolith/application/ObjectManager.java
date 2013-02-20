package com.geargames.regolith.application;

import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Класс управляет сущностями клиента. Сделан для того, чтоб минимизировать затраты на сборку мусора.
 */
public class ObjectManager {
    private static ObjectManager instance;

    private ClientBattleCollection battleCollection;

    private ObjectManager() {
        battleCollection = new ClientBattleCollection();
        battleCollection.setBattles(new Vector());
    }

    public ClientBattleCollection getBattleCollection() {
        return battleCollection;
    }

    public  static ObjectManager getInstance(){
        if(instance == null){
            instance = new ObjectManager();
        }
        return instance;
    }

}

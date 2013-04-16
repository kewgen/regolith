package com.geargames.regolith.service;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Users: mkutuzov, abarakov
 * Date: 15.06.12
 */
public class ConcurrentBattleManagerContext implements BattleManagerContext {
    private ConcurrentMap<Integer, Battle> battlesById;
    private ConcurrentMap<Account, Battle> createdBattles;
    private ConcurrentMap<Account, Battle> battlesByAccount; //todo: Мапы createdBattles и battlesByAccount можно объединить
    private ConcurrentMap<Battle, Set<Account>> listeners;
    private ConcurrentMap<Battle, Set<BattleGroup>> completeGroups;

    public ConcurrentBattleManagerContext() {
        battlesById = new ConcurrentHashMap<Integer, Battle>();
        createdBattles = new ConcurrentHashMap<Account, Battle>();
        battlesByAccount = new ConcurrentHashMap<Account, Battle>();
        listeners = new ConcurrentHashMap<Battle, Set<Account>>();
        completeGroups = new ConcurrentHashMap<Battle, Set<BattleGroup>>();
    }

    /**
     * Получить map всех созданных битв, где ключ - это id битвы.
     */
    @Override
    public ConcurrentMap<Integer, Battle> getBattlesById() {
        return battlesById;
    }

    /**
     * Получить map всех созданных битв, где ключ - это ссылка на автора битвы.
     */
    @Override
    public ConcurrentMap<Account, Battle> getCreatedBattles() {
        return createdBattles;
    }

    /**
     * Получить map всех слушаемых битв каждым клиентом, где ключ - это ссылка на подписчика битвы (ее автора, участника или подписчика).
     */
    @Override
    public ConcurrentMap<Account, Battle> getBattlesByAccount() {
        return battlesByAccount;
    }

    /**
     * Получить map слушателей всех битв.
     * Где ключ - ссылка на созданную битву, значение - множество всех слушателей битвы.
     */
    @Override
    public ConcurrentMap<Battle, Set<Account>> getBattleListeners() {
        return listeners;
    }

    /**
     * Получить map всех заполненных боевых групп всех битв.
     */
    @Override
    public ConcurrentMap<Battle, Set<BattleGroup>> getCompleteGroups() {
        return completeGroups;
    }

}

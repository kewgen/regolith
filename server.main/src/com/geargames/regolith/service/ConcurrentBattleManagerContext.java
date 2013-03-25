package com.geargames.regolith.service;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * User: mkutuzov
 * Date: 15.06.12
 */
public class ConcurrentBattleManagerContext implements BattleManagerContext {
    private ConcurrentMap<Integer, Battle> battles;
    private ConcurrentMap<Account, Battle> created;
    private ConcurrentMap<Battle, Set<Account>> listeners;
    private ConcurrentMap<Battle, Set<BattleGroup>> completeGroups;

    public ConcurrentBattleManagerContext() {
        created = new ConcurrentHashMap<Account, Battle>();
        listeners = new ConcurrentHashMap<Battle, Set<Account>>();
        battles = new ConcurrentHashMap<Integer, Battle>();
        completeGroups = new ConcurrentHashMap<Battle, Set<BattleGroup>>();
    }

    /**
     * ??? Получить map...
     */
    @Override
    public ConcurrentMap<Battle, Set<BattleGroup>> getCompleteGroups() {
        return completeGroups;
    }

    /**
     * Получить map всех созданных битв, где ключ - это id битвы.
     */
    @Override
    public ConcurrentMap<Integer, Battle> getBattlesById() {
        return battles;
    }

    /**
     * Получить map всех созданных битв, где ключ - это ссылка на автора битвы.
     */
    @Override
    public ConcurrentMap<Account, Battle> getCreatedBattles() {
        return created;
    }

    /**
     * Получить map слушателей всех битв.
     * Где ключ - ссылка на созданную битву, значение - множество всех слушателей битвы.
     */
    @Override
    public ConcurrentMap<Battle, Set<Account>> getBattleListeners() {
        return listeners;
    }
}

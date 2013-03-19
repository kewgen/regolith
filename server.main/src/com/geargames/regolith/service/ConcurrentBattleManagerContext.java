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


    @Override
    public ConcurrentMap<Battle, Set<BattleGroup>> getCompleteGroups() {
        return completeGroups;
    }

    @Override
    public ConcurrentMap<Integer, Battle> getBattlesById() {
        return battles;
    }

    @Override
    public ConcurrentMap<Account, Battle> getCreatedBattles() {
        return created;
    }

    @Override
    public ConcurrentMap<Battle, Set<Account>> getBattleListeners() {
        return listeners;
    }
}

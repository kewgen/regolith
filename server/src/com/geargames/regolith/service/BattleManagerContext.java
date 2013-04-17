package com.geargames.regolith.service;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Users: mkutuzov, abarakov
 * Date: 15.06.12
 */
public interface BattleManagerContext {

    ConcurrentMap<Integer, Battle> getBattlesById();
    ConcurrentMap<Account, Battle> getCreatedBattles();
    ConcurrentMap<Account, Battle> getBattlesByAccount();
    ConcurrentMap<Battle, Set<Account>> getBattleListeners();
    ConcurrentMap<Battle, Set<BattleGroup>> getCompleteGroups();

}

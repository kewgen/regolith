package com.geargames.regolith.service;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;


import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * User: mkutuzov
 * Date: 15.06.12
 */
public interface BattleManagerContext {
    ConcurrentMap<Battle, Set<BattleGroup>> getCompleteGroups();
    ConcurrentMap<Integer, Battle> getBattlesById();
    ConcurrentMap<Account, Battle> getCreatedBattles();
    ConcurrentMap<Battle, Set<Account>> getBattleListeners();
}

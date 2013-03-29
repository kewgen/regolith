package com.geargames.regolith.helpers;

import com.geargames.regolith.units.battle.BattleType;

/**
 * User: abarakov
 * Date: 27.03.13
 */
public class BattleTypeHelper {

    public static BattleType findBattleTypeById(int id, BattleType[] entities) {
        for (int i = 0; i < entities.length; i++) {
            BattleType battleType = entities[i];
            if (id == battleType.getId()) {
                return battleType;
            }
        }
        throw new IllegalArgumentException();
    }

}
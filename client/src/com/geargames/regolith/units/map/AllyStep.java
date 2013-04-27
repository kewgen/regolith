package com.geargames.regolith.units.map;

import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mvkutuzov
 * Date: 24.04.13
 * Time: 11:58
  */
public class AllyStep extends Step {

    protected void routeMap(Warrior warrior) {
        ClientBattleHelper.route(warrior, getBattleConfiguration().getRouter());
    }

    protected boolean hasToStop(Warrior warrior) {
        return WarriorHelper.getReachableRadius(warrior) == 0;
    }

    protected void doStepOnMap(Warrior warrior, int stepX, int stepY) {
        WarriorHelper.step(warrior, stepX, stepY, getBattleConfiguration());
    }

}

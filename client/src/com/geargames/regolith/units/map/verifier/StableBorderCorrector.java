package com.geargames.regolith.units.map.verifier;

import com.geargames.regolith.ClientBattleContext;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.map.MapCorrector;

/**
 * Users: mkutuzov, abarakov
 * Date: 15.02.12
 * Time: 23:28
 */
public class StableBorderCorrector extends MapCorrector {
    private Pair pair;

    public StableBorderCorrector() {
        pair = new Pair();
    }

    @Override
    public Pair correct(int x, int y, BattleScreen battleScreen) {
        if (x < -ClientBattleContext.HORIZONTAL_RADIUS) {
            pair.setX(-ClientBattleContext.HORIZONTAL_RADIUS);
        } else if (x > 0) {
            pair.setX(0);
        } else {
            pair.setX(x);
        }
        if (y < -ClientBattleContext.VERTICAL_RADIUS) {
            pair.setY(-ClientBattleContext.VERTICAL_RADIUS);
        } else if (y > 0) {
            pair.setY(0);
        } else {
            pair.setY(y);
        }
        return pair;
    }

}

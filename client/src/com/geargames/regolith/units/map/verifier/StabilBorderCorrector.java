package com.geargames.regolith.units.map.verifier;

import com.geargames.regolith.ClientBattleContext;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.map.MapCorrector;

/**
 * User: mkutuzov
 * Date: 15.02.12
 * Time: 23:28
 */
public class StabilBorderCorrector extends MapCorrector {

    public void correct(int x, int y, BattleScreen battleScreen) {
        if (x < -ClientBattleContext.HORIZONTAL_RADIUS) {
            battleScreen.setMapX(-ClientBattleContext.HORIZONTAL_RADIUS);
        } else if (x > 0) {
            battleScreen.setMapX(0);
        }
        if (y < -ClientBattleContext.VERTICAL_RADIUS) {
            battleScreen.setMapY(-ClientBattleContext.VERTICAL_RADIUS);
        } else if (y > 0) {
            battleScreen.setMapY(0);
        }
    }

}

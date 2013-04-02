package com.geargames.regolith.units.map.verifier;

import com.geargames.regolith.Port;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.map.MapCorrector;

/**
 * User: mkutuzov
 * Date: 15.02.12
  */
public class CubeBorderCorrector extends MapCorrector {
    public void correct(int x, int y, BattleScreen battleScreen) {
        BattleMap battleMap = battleScreen.getBattle().getMap();
        if (x < -BattleScreen.HORIZONTAL_RADIUS) {
            battleScreen.setMapX(-BattleScreen.HORIZONTAL_RADIUS);
        } else if (x > battleMap.getCells().length * BattleScreen.HORIZONTAL_DIAGONAL - Port.getW() - BattleScreen.HORIZONTAL_RADIUS) {
            battleScreen.setMapX(battleMap.getCells().length * BattleScreen.HORIZONTAL_DIAGONAL - Port.getW() - BattleScreen.HORIZONTAL_RADIUS);
        }
        if (y < -BattleScreen.VERTICAL_RADIUS) {
            battleScreen.setMapY(-BattleScreen.VERTICAL_RADIUS);
        } else if (y > battleMap.getCells().length * BattleScreen.VERTICAL_DIAGONAL - Port.getH() - BattleScreen.VERTICAL_RADIUS) {
            battleScreen.setMapY(battleMap.getCells().length * BattleScreen.VERTICAL_DIAGONAL - Port.getH() - BattleScreen.VERTICAL_RADIUS);
        }
    }
}

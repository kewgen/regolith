package com.geargames.regolith.units.map.verifier;

import com.geargames.regolith.ClientBattleContext;
import com.geargames.regolith.ClientConfigurationFactory;
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
        BattleMap battleMap = ClientConfigurationFactory.getConfiguration().getBattleContext().getBattle().getMap();
        if (x < -ClientBattleContext.HORIZONTAL_RADIUS) {
            battleScreen.setMapX(-ClientBattleContext.HORIZONTAL_RADIUS);
        } else if (x > battleMap.getCells().length * ClientBattleContext.HORIZONTAL_DIAGONAL - Port.getW() - ClientBattleContext.HORIZONTAL_RADIUS) {
            battleScreen.setMapX(battleMap.getCells().length * ClientBattleContext.HORIZONTAL_DIAGONAL - Port.getW() - ClientBattleContext.HORIZONTAL_RADIUS);
        }
        if (y < -ClientBattleContext.VERTICAL_RADIUS * 3) {
            battleScreen.setMapY(-ClientBattleContext.VERTICAL_RADIUS * 3);
        } else if (y > battleMap.getCells().length * ClientBattleContext.VERTICAL_DIAGONAL - Port.getH() - ClientBattleContext.VERTICAL_RADIUS) {
            battleScreen.setMapY(battleMap.getCells().length * ClientBattleContext.VERTICAL_DIAGONAL - Port.getH() - ClientBattleContext.VERTICAL_RADIUS);
        }
    }

}

package com.geargames.regolith.units.map.verifier;

import com.geargames.regolith.ClientBattleContext;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Port;
import com.geargames.regolith.map.Pair;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.map.MapCorrector;

/**
 * Users: mkutuzov, abarakov
 * Date: 15.02.12
 */
public class CubeBorderCorrector extends MapCorrector {
    private Pair pair;

    public CubeBorderCorrector() {
        pair = new Pair();
    }

    @Override
    public Pair correct(int x, int y, BattleScreen battleScreen) {
        BattleMap battleMap = ClientConfigurationFactory.getConfiguration().getBattleContext().getBattle().getMap();
        final int minX = -ClientBattleContext.HORIZONTAL_RADIUS * 3; //todo: Использовать battleScreen.getMapMinX()
        if (x < minX) {
            pair.setX(minX);
        } else {
            int maxX = battleMap.getCells().length * ClientBattleContext.HORIZONTAL_DIAGONAL - Port.getW() + ClientBattleContext.HORIZONTAL_RADIUS;  //todo: Использовать battleScreen.getMapMaxX()
            if (x > maxX) {
                pair.setX(maxX);
            } else {
                pair.setX(x);
            }
        }
        final int minY = -ClientBattleContext.VERTICAL_RADIUS * 4; //todo: Использовать battleScreen.getMapMinY()
        if (y < minY) {
            pair.setY(minY);
        } else {
            int maxY = battleMap.getCells().length * ClientBattleContext.VERTICAL_DIAGONAL - Port.getH() + ClientBattleContext.VERTICAL_RADIUS;  //todo: Использовать battleScreen.getMapMaxY()
            if (y > maxY) {
                pair.setY(maxY);
            } else {
                pair.setY(y);
            }
        }
        return pair;
    }

}

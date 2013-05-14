package com.geargames.regolith.units.map.finder;

import com.geargames.regolith.ClientBattleContext;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.map.Finder;
import com.geargames.regolith.map.Pair;

/**
 * User: mkutuzov
 * Date: 20.02.12
 * Предназначен для поиска экранных координат середины ячейки.
 */
public class ReverseProjectionFinder extends Finder {
    private Pair map;

    public ReverseProjectionFinder() {
        map = new Pair();
    }

    public Pair find(int x, int y, BattleScreen battleScreen) {
        Pair topCenter = battleScreen.getTopCenter();

        double k = ClientBattleContext.TANGENT;
        int leftY = topCenter.getY() + ClientBattleContext.VERTICAL_RADIUS * x;
        int leftX = topCenter.getX() - ClientBattleContext.HORIZONTAL_RADIUS * x;
        int leftB = leftY - (int) (leftX * k);

        int rightY = topCenter.getY() + ClientBattleContext.VERTICAL_RADIUS * y;
        int rightX = topCenter.getX() + ClientBattleContext.HORIZONTAL_RADIUS * y;
        int rightB = rightY + (int) (rightX * k);

        int xx = (rightB - leftB) / (int) (2 * k);
        int yy = -(int) (k * xx) + rightB;
        map.setX(xx);
        map.setY(yy);

        return map;
    }

}

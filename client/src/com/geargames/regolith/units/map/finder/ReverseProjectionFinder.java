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

    public Pair find(int left, int right, BattleScreen battleScreen) {
        Pair topCenter = battleScreen.getTopCenter();

        double k = ClientBattleContext.TANGENT;
        int leftY = topCenter.getY() + ClientBattleContext.VERTICAL_RADIUS * left;
        int leftX = topCenter.getX() - ClientBattleContext.HORIZONTAL_RADIUS * left;
        int leftB = leftY - (int) (leftX * k);

        int rightY = topCenter.getY() + ClientBattleContext.VERTICAL_RADIUS * right;
        int rightX = topCenter.getX() + ClientBattleContext.HORIZONTAL_RADIUS * right;
        int rightB = rightY + (int) (rightX * k);

        int x = (rightB - leftB) / (int) (2 * k);
        int y = -(int) (k * x) + rightB;
        map.setX(x);
        map.setY(y);

        return map;
    }

}

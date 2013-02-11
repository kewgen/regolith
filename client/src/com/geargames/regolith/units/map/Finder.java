package com.geargames.regolith.units.map;

import com.geargames.regolith.map.Pair;

/**
 * User: mkutuzov
 * Date: 16.02.12
 */
public abstract class Finder {
    public abstract Pair find(int x, int y, BattleScreen battleScreen);
}

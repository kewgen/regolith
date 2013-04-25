package com.geargames.regolith.units.map;

import com.geargames.regolith.units.battle.MoveOneStepListener;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mvkutuzov
 * Date: 25.04.13
 * Time: 14:41
 */
public class NullStepListener extends MoveOneStepListener {
    public static final NullStepListener instance = new NullStepListener();

    private NullStepListener() {
    }

    @Override
    public void onStep(Warrior warrior, int x, int y) {
    }
}

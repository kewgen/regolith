package com.geargames.regolith.units.map;

import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 23.07.12
 */
public abstract class MoveOneStepListener {

    public abstract void onStep(Warrior warrior, int x, int y);

}

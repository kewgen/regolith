package com.geargames.regolith.units.map.states;

/**
 * User: mvkutuzov
 * Date: 25.04.13
 * Time: 9:55
  */
public interface UnitFinishListener {
    void onFinish(AbstractWarriorState previous, AbstractWarriorState next);
}

package com.geargames.regolith.units.map;

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
    public void onStep(HumanElement unit, int x, int y) {
    }
}

package com.geargames.regolith.units.map.states;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public abstract class LinearState extends AbstractState {
    @Override
    public void next() {
        current++;
        if (current == limit) {
            current = (byte) (limit - 1);
            onFinish();
        }
    }

    public abstract void onFinish();
}

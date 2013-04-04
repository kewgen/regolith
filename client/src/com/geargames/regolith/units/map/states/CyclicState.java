package com.geargames.regolith.units.map.states;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public abstract class CyclicState extends AbstractState {
    @Override
    public void next() {
        current++;
        if (current == limit) {
            current = 0;
        }
    }

}

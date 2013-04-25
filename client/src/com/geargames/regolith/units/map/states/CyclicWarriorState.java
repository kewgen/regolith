package com.geargames.regolith.units.map.states;

import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public abstract class CyclicWarriorState extends AbstractWarriorState {
    @Override
    public void next(Unit unit) {
        current++;
        current%=limit;
        if(current == 0 && getListener()!=null){
            getListener().onFinish(this, this);
        }
    }

    @Override
    public PUnit current() {
        return (PUnit)defaultScript.getIndex(current).getPrototype();
    }

}

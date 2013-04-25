package com.geargames.regolith.units.map.states;

import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class LinearWarriorState extends AbstractWarriorState {
    private AbstractWarriorState finishState;
    private byte action;

    @Override
    public void next(Unit unit) {
        current++;
        if (current >= limit) {
            getFinishState().init(unit);
            if (getListener() != null) {
                getListener().onFinish(this, getFinishState());
            }
        }
    }

    @Override
    public PUnit current() {
        return (PUnit) defaultScript.getIndex(current).getPrototype();
    }

    public void setFinishState(AbstractWarriorState finishState) {
        this.finishState = finishState;
    }

    public AbstractWarriorState getFinishState() {
        return finishState;
    }

    @Override
    public void change(Unit unit, AbstractWarriorState state) {
    }

    @Override
    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }
}

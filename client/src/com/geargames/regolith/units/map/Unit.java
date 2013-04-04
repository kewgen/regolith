package com.geargames.regolith.units.map;

import com.geargames.common.Render;
import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.states.AbstractState;
import com.geargames.regolith.units.map.states.Actions;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class Unit {
    private Warrior warrior;
    private AbstractState state;

    public PUnit sitDown(Render render){
        return state.getPUnit(render,this, Actions.SIT_DOWN);
    }

    public PUnit standUp(Render render){
        return state.getPUnit(render,this, Actions.STAND_UP);
    }

    public PUnit run(Render render){
        return state.getPUnit(render,this, Actions.RUN);
    }

    public PUnit stand(Render render){
        return state.getPUnit(render,this, Actions.STAND);
    }

    public PUnit shoot(Render render){
        if(warrior.isHalfLong()){
            return state.getPUnit(render,this, Actions.SIT_AND_SHOOT);
        }else{
            return state.getPUnit(render,this, Actions.STAND_AND_SHOOT);
        }
    }

    public PUnit hit(Render render){
        if(warrior.isHalfLong()){
            return state.getPUnit(render,this, Actions.SIT_AND_HIT);
        }else{
            return state.getPUnit(render,this, Actions.STAND_AND_HIT);
        }
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    public AbstractState getState() {
        return state;
    }

    public void setState(AbstractState state) {
        this.state = state;
    }
}

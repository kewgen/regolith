package com.geargames.regolith.units;

import com.geargames.awt.Eventable;
import com.geargames.common.Graphics;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.states.*;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class Unit extends Eventable {
    private Warrior warrior;
    private AbstractWarriorState state;

    private Stand stand;
    private Sit sit;
    private Run run;
    private Die die;

    private LinearWarriorState process;

    public void init() {
        stand = new Stand();
        sit = new Sit();
        die = new Die();
        run = new Run();
        process = new LinearWarriorState();
        stand.init(this);
    }

    public void sitDown() {
        if (state != process) {
            process.setAction(Actions.SIT_DOWN);
            process.setFinishState(sit);
            state.change(this, process);
        }
    }

    public void standUp() {
        if (state != process) {
            process.setAction(Actions.STAND_UP);
            process.setFinishState(stand);
            state.change(this, process);
        }
    }

    public void run() {
        state.change(this, run);
    }

    public void stop() {
        state.change(this, stand);
    }

    public void shoot() {
        if (state != process) {
            process.setFinishState(state);
            if (warrior.isSitting()) {
                process.setAction(Actions.SIT_AND_SHOOT);
            } else {
                process.setAction(Actions.STAND_AND_SHOOT);
            }
            state.change(this, process);
        }
    }

    public void hit() {
        if (state != process) {
            process.setFinishState(state);
            if (warrior.isSitting()) {
                process.setAction(Actions.SIT_AND_HIT);
            } else {
                process.setAction(Actions.STAND_AND_HIT);
            }
            state.change(this, process);
        }
    }

    public void die() {
        if (state != process) {
            process.setFinishState(die);
            if (warrior.isHalfLong()) {
                process.setAction(Actions.SIT_AND_DIE);
            } else {
                process.setAction(Actions.STAND_AND_DIE);
            }
            state.change(this, process);
        }
    }

    public void next() {
        state.next(this);
    }

    public void draw(Graphics graphics, int x, int y) {
        state.current().draw(graphics, x, y, warrior);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    public AbstractWarriorState getState() {
        return state;
    }

    public void setState(AbstractWarriorState state) {
        this.state = state;
    }

    /**
     * Вернет true, если боец бездействует.
     * @return
     */
    public boolean isIdleState() {
        return state == stand || state == sit;
    }

}

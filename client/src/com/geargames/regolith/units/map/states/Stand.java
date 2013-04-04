package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.common.packer.PUnitScript;
import com.geargames.regolith.units.map.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class Stand extends StaticState {

    @Override
    protected PUnitScript getPUnitScript(Render render, Unit unit, int action) {
        AbstractState state = null;
        switch (action) {
            case Actions.SIT_DOWN:
                state = new SitDown();
                break;
            case Actions.RUN:
                state = new Run();
                break;
            case Actions.STAND_AND_HIT:
                state = new StandHit();
                break;
            case Actions.STAND_AND_SHOOT:
                state = new StandShoot();
                break;
        }
        if (state == null) {
            return defaultScript;
        } else {
            state.init(render, unit, action);
            unit.setState(state);
            return state.getPUnitScript(render, unit, action);
        }
    }
}

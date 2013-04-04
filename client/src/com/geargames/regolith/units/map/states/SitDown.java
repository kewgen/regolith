package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.common.packer.PUnitScript;
import com.geargames.regolith.units.map.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class SitDown extends LinearState {
    private boolean finished;

    @Override
    protected PUnitScript getPUnitScript(Render render, Unit unit, int action) {
        if (!finished) {
            return defaultScript;
        } else {
            Stand stand = new Stand();
            stand.init(render, unit, action);
            unit.setState(stand);
            return stand.getPUnitScript(render, unit, action);
        }
    }

    @Override
    public void init(Render render, Unit unit, int action) {
        super.init(render, unit, action);
        finished = false;
    }

    @Override
    public void onFinish() {
        finished = true;
    }
}

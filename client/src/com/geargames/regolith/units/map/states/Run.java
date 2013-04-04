package com.geargames.regolith.units.map.states;

import com.geargames.common.Render;
import com.geargames.common.packer.PUnitScript;
import com.geargames.regolith.units.map.Unit;

/**
 * User: m.v.kutuzov
 * Date: 03.04.13
 */
public class Run extends CyclicState {

    @Override
    protected PUnitScript getPUnitScript(Render render, Unit unit, int action) {
        switch (action) {
            case Actions.STAND:
                Stand stand = new Stand();
                stand.init(render, unit, action);
                unit.setState(stand);
                return stand.getPUnitScript(render, unit, action);
        }
        return defaultScript;
    }

}

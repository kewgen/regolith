package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.CellElementTypes;

/**
 * User: mkutuzov
 * Date: 03.05.12
 */
public class Harvester extends Tackle {

    public int getType() {
        return TackleType.HARVESTER;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.HARVESTER;
    }
}

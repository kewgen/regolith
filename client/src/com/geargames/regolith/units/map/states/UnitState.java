package com.geargames.regolith.units.map.states;

import com.geargames.common.packer.PUnit;
import com.geargames.regolith.units.Unit;

/**
 * User: m.v.kutuzov
 * Date: 02.04.13
 */
public abstract class UnitState {

    /**
     * Перейти на следующее состояние из PUnitScript.
     */
    public abstract void next( Unit unit);

    /**
     * Вернуть текущий PUnit для отображения бойца.
     * @return
     */
    public abstract PUnit current();

}

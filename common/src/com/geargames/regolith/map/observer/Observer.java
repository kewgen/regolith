package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.dictionaries.HumanElementCollection;
import com.geargames.regolith.units.map.HumanElement;

/**
 * User: mkutuzov
 * Date: 21.02.12
 */
public abstract class Observer {

    /**
     * Осмотреть окрестности бойца unit в пределах его зоны видимости.
     *
     * @param unit
     * @return коллекция врагов бойца unit, которых он увидел.
     */
    public abstract HumanElementCollection observe(HumanElement unit);

}

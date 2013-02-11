package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.battle.Ally;
import com.geargames.regolith.units.dictionaries.AllyCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

/**
 * User: mkutuzov
 * Date: 21.02.12
 */
public abstract class Observer {
    /**
     * Обозреть окрестности бойца warrior в пределах его зоны видимости.
     * @param warrior
     */
    public abstract AllyCollection observe(Ally warrior);
}

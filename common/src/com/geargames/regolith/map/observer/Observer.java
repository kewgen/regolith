package com.geargames.regolith.map.observer;

import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

/**
 * User: mkutuzov
 * Date: 21.02.12
 */
public abstract class Observer {

    /**
     * Осмотреть окрестности бойца warrior в пределах его зоны видимости.
     *
     * @param warrior
     * @return коллекция врагов бойца warrior, которых он увидел.
     */
    public abstract WarriorCollection observe(Warrior warrior);

}

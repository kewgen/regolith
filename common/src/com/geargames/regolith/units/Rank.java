package com.geargames.regolith.units;

import com.geargames.regolith.units.battle.Warrior;

/**
 * Класс представляет собой звание бойца. Содержит в себе наименование и наименьший уровеньм опыта,
 * достаточный для получния звания, а так же список бойцов обладающих этим званием.
 * User: mkutuzov
 * Date: 10.02.12
 */
public class Rank extends Entity {
    private String name;
    private short experience;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getExperience() {
        return experience;
    }

    public void setExperience(short experience) {
        this.experience = experience;
    }
}

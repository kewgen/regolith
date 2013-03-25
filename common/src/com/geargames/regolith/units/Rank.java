package com.geargames.regolith.units;

/**
 * Класс представляет собой звание бойца. Содержит в себе наименование и наименьший уровень опыта,
 * достаточный для получения звания.
 * User: mkutuzov
 * Date: 10.02.12
 */
public class Rank extends Entity {
    private String name;
    private short experience; //todo: int

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

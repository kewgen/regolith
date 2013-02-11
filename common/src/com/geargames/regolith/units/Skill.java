package com.geargames.regolith.units;

/**
 * User: mkutuzov
 * Date: 10.02.12
 * Навык пользования некоторым типом оружия.
 */
public class Skill extends Entity {
    private byte action;
    private short experience;

    /**
     * Число очков на которое мы увеличиваем базовое воздействие при данном уровне навыка.
     * @return
     */
    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    /**
     * Наименьшее количество опыта необходимое для получения данного уровня навыка.
     * @return
     */
    public short getExperience() {
        return experience;
    }

    public void setExperience(short experience) {
        this.experience = experience;
    }
}

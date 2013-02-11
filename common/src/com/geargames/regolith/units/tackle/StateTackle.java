package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.tackle.AbstractTackle;

/**
 * Любые предметы реального мира.
 * User: mkutuzov
 * Date: 13.03.12
 */
public abstract class StateTackle extends AbstractTackle {
    private short state;
    private short firmness;
    private byte upgrade;

    /**
     * Уровень upgrade.
     * @return
     */
    public byte getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(byte upgrade) {
        this.upgrade = upgrade;
    }

    /**
     * Состояние прдмета, если это зхначение равно 0 с предметом в бою будут происходить разные неприятности.
     * @return
     */
    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    /**
     * Крепкость предмета, состояние предмета не может превосходить крепкость.
     * @return
     */
    public short getFirmness() {
        return firmness;
    }

    public void setFirmness(short firmness) {
        this.firmness = firmness;
    }
}

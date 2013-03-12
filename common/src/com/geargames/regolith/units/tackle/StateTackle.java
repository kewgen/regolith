package com.geargames.regolith.units.tackle;

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
     * Состояние предмета. Если равно 0, то с предметом в бою будут происходить разные неприятности.
     * @return
     */
    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    /**
     * Прочность предмета. Состояние предмета не может превосходить прочность.
     * @return
     */
    public short getFirmness() {
        return firmness;
    }

    public void setFirmness(short firmness) {
        this.firmness = firmness;
    }
}

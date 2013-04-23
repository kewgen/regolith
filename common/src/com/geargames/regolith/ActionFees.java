package com.geargames.regolith;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 17.03.12
 */
public class ActionFees implements Serializable {
    private byte move;
    private byte sitOrStand;
    private byte shutOrOpen;
    private byte changeWeapon;
    private byte pickupTackle;
    private byte rechargeWeapon;

    /**
     * Получить число ОД необходимых для перемещения на одну клетку карты.
     */
    public byte getMove() {
        return move;
    }

    public void setMove(byte move) {
        this.move = move;
    }

    /**
     * Получить число ОД необходимых для того, чтобы боец мог присесть или встать.
     */
    public byte getSitOrStand() {
        return sitOrStand;
    }

    public void setSitOrStand(byte sitOrStand) {
        this.sitOrStand = sitOrStand;
    }

    /**
     * Получить число ОД необходимых для открытия или закрытия двери.
     */
    public byte getShutOrOpen() {
        return shutOrOpen;
    }

    public void setShutOrOpen(byte shutOrOpen) {
        this.shutOrOpen = shutOrOpen;
    }

    /**
     * Получить число ОД необходимых для смены оружия.
     */
    public byte getChangeWeapon() {
        return changeWeapon;
    }

    public void setChangeWeapon(byte changeWeapon) {
        this.changeWeapon = changeWeapon;
    }

    /**
     * Получить число ОД необходимых для поднятия бойцом одного (???) предмета с земли.
     */
    public byte getPickupTackle() {
        return pickupTackle;
    }

    public void setPickupTackle(byte pickupTackle) {
        this.pickupTackle = pickupTackle;
    }

    /**
     * Получить число ОД необходимых для перезарядки оружия.
     */
    public byte getRechargeWeapon() {
        return rechargeWeapon;
    }

    public void setRechargeWeapon(byte rechargeWeapon) {
        this.rechargeWeapon = rechargeWeapon;
    }

}

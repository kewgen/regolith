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

    public byte getMove() {
        return move;
    }

    public void setMove(byte move) {
        this.move = move;
    }

    public byte getSitOrStand() {
        return sitOrStand;
    }

    public void setSitOrStand(byte sitOrStand) {
        this.sitOrStand = sitOrStand;
    }

    public byte getShutOrOpen() {
        return shutOrOpen;
    }

    public void setShutOrOpen(byte shutOrOpen) {
        this.shutOrOpen = shutOrOpen;
    }

    public byte getChangeWeapon() {
        return changeWeapon;
    }

    public void setChangeWeapon(byte changeWeapon) {
        this.changeWeapon = changeWeapon;
    }

    public byte getPickupTackle() {
        return pickupTackle;
    }

    public void setPickupTackle(byte pickupTackle) {
        this.pickupTackle = pickupTackle;
    }

    public byte getRechargeWeapon() {
        return rechargeWeapon;
    }

    public void setRechargeWeapon(byte rechargeWeapon) {
        this.rechargeWeapon = rechargeWeapon;
    }
}

package com.geargames.regolith.units;

import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.Weapon;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mkutuzov
 * Date: 30.03.12
 */
public class Human extends Element {
    private int frameId;
    private String name;
    private Rank rank;
    private int health;

    private Armor headArmor;
    private Armor torsoArmor;
    private Armor legsArmor;
    private Weapon weapon;

    private boolean shooting;
    private boolean sitting;
    private boolean moving;

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public boolean isSitting() {
        return sitting;
    }

    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    public Armor getHeadArmor() {
        return headArmor;
    }

    public void setHeadArmor(Armor headArmor) {
        this.headArmor = headArmor;
    }

    public Armor getTorsoArmor() {
        return torsoArmor;
    }

    public void setTorsoArmor(Armor torsoArmor) {
        this.torsoArmor = torsoArmor;
    }

    public Armor getLegsArmor() {
        return legsArmor;
    }

    public void setLegsArmor(Armor legsArmor) {
        this.legsArmor = legsArmor;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHalfLong() {
        return isSitting();
    }

    public boolean isAbleToLookThrough() {
        return isSitting();
    }

    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return isSitting();
    }

    public boolean isAbleToWalkThrough() {
        return false;
    }

    @Override
    public short getElementType() {
        return ElementTypes.HUMAN;
    }
}

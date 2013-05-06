package com.geargames.regolith.units;

import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * Users: mkutuzov, abarakov
 * Date: 30.03.12
 * Базовый класс для всех бойцов.
 */
public class Human extends Entity {
    public static final byte ENEMY = 1;
    public static final byte ALLY = 2;
    public static final byte WARRIOR = 3; //todo: переименовать

    private BattleGroup battleGroup;
    private short number; //todo: перенести number в battleGroup

    private int frameId;
    private String name;
    private byte membershipType;

    private Rank rank;
    private int health;
    private Armor headArmor;
    private Armor torsoArmor;
    private Armor legsArmor;
    private Weapon weapon;

    public byte getMembershipType() {
        if (membershipType < ENEMY || membershipType > WARRIOR) {
            throw new RuntimeException("Invalid value of MembershipType (membershipType = " + membershipType + ")");
        }
        return membershipType;
    }

    //todo: ????? Не везде проставляется принадлежность бойца
    public void setMembershipType(byte value) {
        membershipType = value;
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }

    public void setBattleGroup(BattleGroup battleGroup) {
        this.battleGroup = battleGroup;
    }

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
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

}

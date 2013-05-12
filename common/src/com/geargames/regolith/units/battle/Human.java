package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.Rank;
import com.geargames.regolith.units.map.DynamicCellElement;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * Users: mkutuzov, abarakov
 * Date: 30.03.12
 * Базовый класс для всех бойцов.
 */
public abstract class Human extends DynamicCellElement {
    public static final byte ENEMY = 1;
    public static final byte ALLY = 2;
    public static final byte WARRIOR = 3; //todo: переименовать

    private BattleGroup battleGroup;
    private short number; //todo: short -> byte

    private int frameId;
    private int avatarFrameId;
    private int avatarMiniFrameId;
    private String name;

    private Rank rank;
    private int health;
    private Armor headArmor;
    private Armor torsoArmor;
    private Armor legsArmor;
    private Weapon weapon;

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

    @Override
    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int unitFrameId) {
        this.frameId = unitFrameId;
    }

    public int getAvatarFrameId() {
        return avatarFrameId;
    }

    public void setAvatarFrameId(int frameId) {
        this.avatarFrameId = frameId;
    }

    public int getAvatarMiniFrameId() {
        return avatarMiniFrameId;
    }

    public void setAvatarMiniFrameId(int frameId) {
        this.avatarMiniFrameId = frameId;
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

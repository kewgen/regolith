package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.dictionaries.AllyCollection;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.Weapon;

import java.util.Date;
import java.util.Hashtable;

/**
 * User: mkutuzov
 * Date: 03.02.12
 */
public class Warrior extends Ally {
    public static final int MOVING_OFFSET = 8;
    public static final int STANDING_SHOOTING_OFFSET = 16;
    public static final int SITTING_OFFSET = 24;
    public static final int SITTING_SHOOTING_OFFSET = 32;
    private short strength;
    private byte speed;
    private byte marksmanship;
    private byte craftiness;
    private byte vitality;
    private Date birthDate;
    private short[] skillScores;
    private Skill[] skills;
    private short actionScore;
    private int experience;
    private Bag bag;
    private AmmunitionBag ammunitionBag;
    private Direction direction;
    private Hashtable victimsDamages;

    /**
     * Сумка с патронами и аптечками.
     * @return
     */
    public AmmunitionBag getAmmunitionBag() {
        return ammunitionBag;
    }

    public void setAmmunitionBag(AmmunitionBag ammunitionBag) {
        this.ammunitionBag = ammunitionBag;
    }

    /**
     * Направление в котором боец повёрнут.
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Таблица урона который боец причинил противникам в течение последнего боя.
     * @return
     */
    public Hashtable getVictimsDamages() {
        return victimsDamages;
    }

    public void setVictimsDamages(Hashtable victimsDamages) {
        this.victimsDamages = victimsDamages;
    }

    /**
     * Сила бойца (измеряется в граммах) .
     * @return
     */
    public short getStrength() {
        return strength;
    }

    public void setStrength(short strength) {
        this.strength = strength;
    }

    /**
     * Скорость перемещения бойца по карте(в клетках за секунду).
     * @return
     */
    public byte getSpeed() {
        return speed;
    }

    public void setSpeed(byte speed) {
        this.speed = speed;
    }

    /**
     * Меткость бойца(в процентах).
     * @return
     */
    public byte getMarksmanship() {
        return marksmanship;
    }

    public void setMarksmanship(byte marksmanship) {
        this.marksmanship = marksmanship;
    }

    /**
     * Ловкость бойца(в процентах).
     * @return
     */
    public byte getCraftiness() {
        return craftiness;
    }

    public void setCraftiness(byte craftiness) {
        this.craftiness = craftiness;
    }

    /**
     * День рождения.
     * @return
     */
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     *
     * @return
     */
    public byte getVitality() {
        return vitality;
    }

    public void setVitality(byte vitality) {
        this.vitality = vitality;
    }

    /**
     * Очки опыта распределённые по категориям оружия.
     * @return
     */
    public short[] getSkillScores() {
        return skillScores;
    }

    public void setSkillScores(short[] skillScores) {
        this.skillScores = skillScores;
    }

    /**
     * Уровни опыта распределённые по категориям оружия.
     * @return
     */
    public Skill[] getSkills() {
        return skills;
    }

    public void setSkills(Skill[] skills) {
        this.skills = skills;
    }

    /**
     * Число очков действия доступное бойцу.
     * @return
     */
    public short getActionScore() {
        return actionScore;
    }

    public void setActionScore(short actionScore) {
        this.actionScore = actionScore;
    }

    /**
     * Количество ОО заработанных бойцом.
     * @return
     */
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Сумка с оружием и бронёй.
     * @return
     */
    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }
}

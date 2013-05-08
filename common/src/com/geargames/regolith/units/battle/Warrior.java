package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.map.CellElementLayers;
import com.geargames.regolith.units.map.CellElementTypes;
import com.geargames.regolith.units.tackle.WeaponCategory;

import java.util.Date;
import java.util.Hashtable;

/**
 * Users: mkutuzov, abarakov
 * Date: 03.02.12
 * Класс бойца, расположенного в одной из клеток на карте.
 */
public class Warrior extends Ally {
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
    private Hashtable victimsDamages;

    private short cellX;
    private short cellY;
    private Direction direction;
    private boolean sitting;

    public Warrior() {
        cellX = 0; //todo: 0 -> -32768
        cellY = 0; //todo: 0 -> -32768
        direction = Direction.NONE;
        sitting = false;
    }

    /**
     * Сумка с патронами и аптечками.
     *
     * @return
     */
    public AmmunitionBag getAmmunitionBag() {
        return ammunitionBag;
    }

    public void setAmmunitionBag(AmmunitionBag ammunitionBag) {
        this.ammunitionBag = ammunitionBag;
    }

    /**
     * Таблица урона который боец причинил противникам в течение последнего боя.
     *
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
     *
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
     *
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
     *
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
     *
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
     *
     * @return
     */
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
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
     *
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
     *
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
     *
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
     *
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
     *
     * @return
     */
    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    //todo: там где апдейтятся cellX и cellY, возможно, следует апдейтить mapX и mapY

    /**
     * Получить номер ячейки по оси X, в которой расположен боец.
     *
     * @return
     */
    public short getCellX() {
        return cellX;
    }

    public void setCellX(short x) {
        this.cellX = x;
    }

    /**
     * Получить номер ячейки по оси Y, в которой расположен боец.
     *
     * @return
     */
    public short getCellY() {
        return cellY;
    }

    public void setCellY(short y) {
        this.cellY = y;
    }

    /**
     * Получить направление, в котором повёрнут боец.
     *
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Вернет true, если боец сидит.
     *
     * @return
     */
    public boolean isSitting() {
        return sitting;
    }

    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    @Override
    public boolean isHalfLong() {
        return sitting;
    }

    @Override
    public boolean isAbleToLookThrough() {
        return sitting;
    }

    @Override
    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return sitting;
    }

    @Override
    public boolean isAbleToWalkThrough() {
        return false;
    }

    @Override
    public boolean isBarrier() {
        return false;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.HUMAN;
    }

    @Override
    public byte getLayer() {
        return CellElementLayers.HUMAN;
    }

}

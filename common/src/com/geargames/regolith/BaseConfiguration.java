package com.geargames.regolith;

import com.geargames.regolith.units.dictionaries.*;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 09.02.12
 */
public class BaseConfiguration implements Serializable {
    private short revision;

    private int money;
    private int regolith;
    private byte pocketsAmount;
    private byte maxWorkShopLevel;
    private byte maxWorkShopProbability;
    private byte minWorkShopProbability;
    private byte vitalityStep;
    private byte strengthStep;
    private byte speedStep;
    private byte marksmanshipStep;
    private byte craftinessStep;
    private byte baseActionScore;
    private short baseHealth;
    private short baseStrength;
    private byte baseCraftiness;
    private byte baseMarksmanship;

    private RankCollection ranks;
    private SkillCollection skills;
    private AmmunitionCategoryCollection ammunitionCategories;
    private ArmorTypeCollection armorTypes;
    private ProjectileCollection projectiles;
    private WeaponCategoryCollection weaponCategories;
    private MedikitCollection medikits;
    private BorderCollection borders;
    private BattleTypeCollection battleTypes;

    private byte initWarriorsAmount;
    private byte maxDistance;
    private byte maxWeight;
    private byte maxDamage;

    /**
     * Наибольшее простреливаемое расстояние.
     * @return
     */
    public byte getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(byte maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * Наибольший вес оружия.
     * @return
     */
    public byte getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(byte maxWeight) {
        this.maxWeight = maxWeight;
    }

    /**
     * Наибольший урон наносимый оружием.
     * @return
     */
    public byte getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(byte maxDamage) {
        this.maxDamage = maxDamage;
    }

    public byte getInitWarriorsAmount() {
        return initWarriorsAmount;
    }

    public void setInitWarriorsAmount(byte initWarriorsAmount) {
        this.initWarriorsAmount = initWarriorsAmount;
    }

    /**
     * Базовое количество денег выделенных аккаунту.
     * @return
     */
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Базовое количество реголита выделенных аккаунту.
     * @return
     */
    public int getRegolith() {
        return regolith;
    }

    public void setRegolith(int regolith) {
        this.regolith = regolith;
    }

    public BattleTypeCollection getBattleTypes() {
        return battleTypes;
    }

    public void setBattleTypes(BattleTypeCollection battleTypes) {
        this.battleTypes = battleTypes;
    }

    public BorderCollection getBorders() {
        return borders;
    }

    public void setBorders(BorderCollection borders) {
        this.borders = borders;
    }

    public MedikitCollection getMedikits() {
        return medikits;
    }

    public void setMedikits(MedikitCollection medikits) {
        this.medikits = medikits;
    }

    public short getBaseHealth() {
        return baseHealth;
    }

    public void setBaseHealth(short baseHealth) {
        this.baseHealth = baseHealth;
    }

    public short getBaseStrength() {
        return baseStrength;
    }

    public void setBaseStrength(short baseStrength) {
        this.baseStrength = baseStrength;
    }

    public byte getBaseCraftiness() {
        return baseCraftiness;
    }

    public void setBaseCraftiness(byte baseCraftiness) {
        this.baseCraftiness = baseCraftiness;
    }

    public byte getBaseMarksmanship() {
        return baseMarksmanship;
    }

    public void setBaseMarksmanship(byte baseMarksmanship) {
        this.baseMarksmanship = baseMarksmanship;
    }

    public byte getBaseActionScore() {
        return baseActionScore;
    }

    public void setBaseActionScore(byte baseActionScore) {
        this.baseActionScore = baseActionScore;
    }

    public byte getVitalityStep() {
        return vitalityStep;
    }

    public void setVitalityStep(byte vitalityStep) {
        this.vitalityStep = vitalityStep;
    }

    public byte getStrengthStep() {
        return strengthStep;
    }

    public void setStrengthStep(byte strengthStep) {
        this.strengthStep = strengthStep;
    }

    public byte getSpeedStep() {
        return speedStep;
    }

    public void setSpeedStep(byte speedStep) {
        this.speedStep = speedStep;
    }

    public byte getMarksmanshipStep() {
        return marksmanshipStep;
    }

    public void setMarksmanshipStep(byte marksmanshipStep) {
        this.marksmanshipStep = marksmanshipStep;
    }

    public byte getCraftinessStep() {
        return craftinessStep;
    }

    public void setCraftinessStep(byte craftinessStep) {
        this.craftinessStep = craftinessStep;
    }

    public AmmunitionCategoryCollection getAmmunitionCategories() {
        return ammunitionCategories;
    }

    public void setAmmunitionCategories(AmmunitionCategoryCollection ammunitionCategories) {
        this.ammunitionCategories = ammunitionCategories;
    }

    public ProjectileCollection getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(ProjectileCollection projectiles) {
        this.projectiles = projectiles;
    }

    public short getRevision() {
        return revision;
    }

    public void setRevision(short revision) {
        this.revision = revision;
    }

    public ArmorTypeCollection getArmorTypes() {
        return armorTypes;
    }

    public void setArmorTypes(ArmorTypeCollection armorTypes) {
        this.armorTypes = armorTypes;
    }

    public WeaponCategoryCollection getWeaponCategories() {
        return weaponCategories;
    }

    public void setWeaponCategories(WeaponCategoryCollection weaponCategories) {
        this.weaponCategories = weaponCategories;
    }

    public byte getMaxWorkShopProbability() {
        return maxWorkShopProbability;
    }

    public void setMaxWorkShopProbability(byte maxWorkShopProbability) {
        this.maxWorkShopProbability = maxWorkShopProbability;
    }

    public byte getMinWorkShopProbability() {
        return minWorkShopProbability;
    }

    public void setMinWorkShopProbability(byte minWorkShopProbability) {
        this.minWorkShopProbability = minWorkShopProbability;
    }

    /**
     * Вернуть наибольший уровень развития мастерской.
     * @return
     */
    public byte getMaxWorkShopLevel() {
        return maxWorkShopLevel;
    }

    public void setMaxWorkShopLevel(byte maxWorkShopLevel) {
        this.maxWorkShopLevel = maxWorkShopLevel;
    }

    /**
     * Количество карманов в сумке аммуниции.
     * @return
     */
    public byte getPocketsAmount() {
        return pocketsAmount;
    }

    public void setPocketsAmount(byte pocketsAmount) {
        this.pocketsAmount = pocketsAmount;
    }

    /**
     * Вернуть массив званий бойца.
     * @return
     */
    public RankCollection getRanks() {
        return ranks;
    }

    public void setRanks(RankCollection ranks) {
        this.ranks = ranks;
    }

    /**
     * Вернуть навыки пользования оружием.
     * @return
     */
    public SkillCollection getSkills() {
        return skills;
    }

    public void setSkills(SkillCollection skills) {
        this.skills = skills;
    }
}

package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.CellElementTypes;
import com.geargames.regolith.units.Skill;
import com.geargames.regolith.units.dictionaries.ProjectileCollection;

/**
 * Позиция из каталога оружия. Хранит базовые характеристики.
 * User: mkutuzov
 * Date: 03.02.12
 */
public class WeaponType extends Tackle {
    private ProjectileCollection projectiles;
    private WeaponDistances distance;
    private WeaponDamage minDamage;
    private WeaponDamage maxDamage;
    private WeaponCategory category;
    private byte accuracy;
    private short capacity;
    private byte ammunitionPerShoot;
    private byte quickAction; //todo: quickAction -> hastilyAction
    private byte accurateAction;
    private short criticalDamage;
    private short baseFirmness;
    private Skill minSkill;

    public WeaponCategory getCategory() {
        return category;
    }

    public void setCategory(WeaponCategory category) {
        this.category = category;
    }

    public Skill getMinSkill() {
        return minSkill;
    }

    public void setMinSkill(Skill minSkill) {
        this.minSkill = minSkill;
    }

    public short getBaseFirmness() {
        return baseFirmness;
    }

    public void setBaseFirmness(short baseFirmness) {
        this.baseFirmness = baseFirmness;
    }

    public ProjectileCollection getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(ProjectileCollection projectiles) {
        this.projectiles = projectiles;
    }

    public WeaponDistances getDistance() {
        return distance;
    }

    public void setDistance(WeaponDistances distance) {
        this.distance = distance;
    }

    public WeaponDamage getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(WeaponDamage minDamage) {
        this.minDamage = minDamage;
    }

    public WeaponDamage getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(WeaponDamage maxDamage) {
        this.maxDamage = maxDamage;
    }

    public byte getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(byte accuracy) {
        this.accuracy = accuracy;
    }

    public short getCapacity() {
        return capacity;
    }

    public void setCapacity(short capacity) {
        this.capacity = capacity;
    }

    public byte getAmmunitionPerShoot() {
        return ammunitionPerShoot;
    }

    public void setAmmunitionPerShoot(byte ammunitionPerShoot) {
        this.ammunitionPerShoot = ammunitionPerShoot;
    }

    /**
     * Получить число ОД необходимых для совершения выстрела "наспех".
     */
    public byte getQuickAction() {
        return quickAction;
    }

    public void setQuickAction(byte quickAction) {
        this.quickAction = quickAction;
    }

    /**
     * Получить число ОД необходимых для совершения прицельного выстрела.
     */
    public byte getAccurateAction() {
        return accurateAction;
    }

    public void setAccurateAction(byte accurateAction) {
        this.accurateAction = accurateAction;
    }

    /**
     *
     * @return
     */
    public short getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(short criticalDamage) {
        this.criticalDamage = criticalDamage;
    }

    public int getType() {
        return TackleType.TYPE;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.NULL;
    }

}

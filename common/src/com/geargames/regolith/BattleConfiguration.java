package com.geargames.regolith;

import com.geargames.regolith.map.observer.Observer;
import com.geargames.regolith.map.router.Router;
import com.geargames.regolith.units.SubordinationDamage;
import com.geargames.regolith.units.Underload;

import java.io.Serializable;

/**
 * User: mkutuzov
 * Date: 09.02.12
 */
public class BattleConfiguration implements Serializable {
    private short revision;
    private Router router;
    private Observer observer;
    private SubordinationDamage[] subordinationDamage;
    private Underload[] underloads;
    private ActionFees actionFees;
    private int walkSpeed;
    private int weaponSpoiling;
    private int armorSpoiling;
    private int activeTime;
    private int minimalMapSize;

    private double accurateShootFix;
    private double quickShootFix;

    private double sitHunterShootFix;
    private double standHunterShootFix;

    private double sitVictimShootFix;
    private double standVictimShootFix;

    private int criticalBarrierToVictimDistance;

    private int killExperienceMultiplier;
    private short abilityMax;


    public short getRevision() {
        return revision;
    }

    public void setRevision(short revision) {
        this.revision = revision;
    }

    public Underload[] getUnderloads() {
        return underloads;
    }

    public void setUnderloads(Underload[] underloads) {
        this.underloads = underloads;
    }

    /**
     * Вернуть список "плата бойца за действие".
     * @return
     */
    public ActionFees getActionFees() {
        return actionFees;
    }

    public void setActionFees(ActionFees actionFees) {
        this.actionFees = actionFees;
    }

    public short getAbilityMax() {
        return abilityMax;
    }

    public void setAbilityMax(short abilityMax) {
        this.abilityMax = abilityMax;
    }

    /**
     * Во сколько раз увеличим число очков опыта заработанных на попадении в бойца за его убийство.
     * @return
     */
    public int getKillExperienceMultiplier() {
        return killExperienceMultiplier;
    }

    public void setKillExperienceMultiplier(int killExperienceMultiplier) {
        this.killExperienceMultiplier = killExperienceMultiplier;
    }

    /**
     * Если процент пути(в рассояние между жертвой и стрелком) от жертвы до ближайщего к нему
     * припятсвия привысит это значение  прикикрытие не будет прикрывать жертву.
     * @return
     */
    public int getCriticalBarrierToVictimDistance() {
        return criticalBarrierToVictimDistance;
    }

    public void setCriticalBarrierToVictimDistance(int criticalBarrierToVictimDistance) {
        this.criticalBarrierToVictimDistance = criticalBarrierToVictimDistance;
    }

    /**
     * Влияние прицельного выстрела на вероятность попадения в цель.
     * @return
     */
    public double getAccurateShootFix() {
        return accurateShootFix;
    }

    public void setAccurateShootFix(double accurateShootFix) {
        this.accurateShootFix = accurateShootFix;
    }

    /**
     * Влияние выстрела наспех на вероятность попадения в цель.
     * @return
     */
    public double getQuickShootFix() {
        return quickShootFix;
    }

    public void setQuickShootFix(double quickShootFix) {
        this.quickShootFix = quickShootFix;
    }

    /**
     * Влияние выстрела сидя на вероятность попадения в цель.
     * @return
     */
    public double getSitHunterShootFix() {
        return sitHunterShootFix;
    }

    public void setSitHunterShootFix(double sitHunterShootFix) {
        this.sitHunterShootFix = sitHunterShootFix;
    }

    /**
     * Влияние выстрела стоя на вероятность попадения в цель.
     * @return
     */
    public double getStandHunterShootFix() {
        return standHunterShootFix;
    }

    public void setStandHunterShootFix(double standHunterShootFix) {
        this.standHunterShootFix = standHunterShootFix;
    }

    /**
     * Влияние присевшей жертвы на вероятность попадения в неё.
     * @return
     */
    public double getSitVictimShootFix() {
        return sitVictimShootFix;
    }

    public void setSitVictimShootFix(double sitVictimShootFix) {
        this.sitVictimShootFix = sitVictimShootFix;
    }

    /**
     * Влияние стоящей прямо жертвы на вероятность попадения в неё.
     * @return
     */
    public double getStandVictimShootFix() {
        return standVictimShootFix;
    }

    public void setStandVictimShootFix(double standVictimShootFix) {
        this.standVictimShootFix = standVictimShootFix;
    }

    /**
     * Наименьший разм ер карты.
     * @return
     */
    public int getMinimalMapSize() {
        return minimalMapSize;
    }

    public void setMinimalMapSize(int minimalMapSize) {
        this.minimalMapSize = minimalMapSize;
    }

    /**
     * Вернуть время (в секундах) предоставленное пользователю на ход.
     * @return
     */
    public int getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(int activeTime) {
        this.activeTime = activeTime;
    }

    /**
     * Вернуть коэфициент порчи бронни при попадении.
     * @return
     */
    public int getArmorSpoiling() {
        return armorSpoiling;
    }

    public void setArmorSpoiling(int armorSpoiling) {
        this.armorSpoiling = armorSpoiling;
    }

    /**
     * Вернуть кличество очков порчи оружия при выстреле.
     * @return
     */
    public int getWeaponSpoiling() {
        return weaponSpoiling;
    }

    public void setWeaponSpoiling(int weaponSpoiling) {
        this.weaponSpoiling = weaponSpoiling;
    }

    /**
     * Вернуть время за которое боец проходит одну клетку карты.
     * @return
     */
    public int getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(int walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    /**
     * Вернуть класс расчёта зоны досягаемости бойца.
     * @return
     */
    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    /**
     * Вернуть класс обозреватель окрестностей бойца.
     * @return
     */
    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    /**
     * Вернуть таблицу разница в звании / поражение за которое даётся одно ОО.
     * @return
     */
    public SubordinationDamage[] getSubordinationDamage() {
        return subordinationDamage;
    }

    public void setSubordinationDamage(SubordinationDamage[] subordinationDamage) {
        this.subordinationDamage = subordinationDamage;
    }
}

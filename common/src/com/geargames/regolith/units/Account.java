package com.geargames.regolith.units;

import com.geargames.regolith.SecurityOperationManager;
import com.geargames.regolith.units.base.Base;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

/**
 * User: mkutuzov
 * Date: 18.03.12
 */
public class Account extends Login {
    private int frameId;
    private Base base;
    private SecurityOperationManager security;
    private WarriorCollection warriors;
    private Clan clan;
    private int experience;

    private int money;
    private int specialist;
    private int coach;
    private int economist;
    private int breadwinner;
    private int fortunate;

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getSpecialist() {
        return specialist;
    }

    public void setSpecialist(int specialist) {
        this.specialist = specialist;
    }

    public int getCoach() {
        return coach;
    }

    public void setCoach(int coach) {
        this.coach = coach;
    }

    public int getEconomist() {
        return economist;
    }

    public void setEconomist(int economist) {
        this.economist = economist;
    }

    public int getBreadwinner() {
        return breadwinner;
    }

    public void setBreadwinner(int breadwinner) {
        this.breadwinner = breadwinner;
    }

    public int getFortunate() {
        return fortunate;
    }

    public void setFortunate(int fortunate) {
        this.fortunate = fortunate;
    }

    /**
     * Получить величину опыта игрока.
     * @return
     */
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Получить клан, к которому принадлежит игрок.
     * @return
     */
    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public SecurityOperationManager getSecurity() {
        return security;
    }

    public void setSecurity(SecurityOperationManager security) {
        this.security = security;
    }

    /**
     * Получить список бойцов игрока.
     * @return
     */
    public WarriorCollection getWarriors() {
        return warriors;
    }

    public void setWarriors(WarriorCollection warriors) {
        this.warriors = warriors;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    @Override
    public String toString() {
        return super.toString() + "; name='" + getName() + "'";
    }

}

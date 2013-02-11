package com.geargames.regolith.units.base;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Entity;

/**
 * User: mkutuzov
 * Date: 18.03.12
 */
public class Base extends Entity {
    private Account account;
    //todo решить являются ли остальные поля самостоятельными сущностями на уровне бд
    private ClearingShop clearingShop;
    private Hospital hospital;
    private RestHouse restHouse;
    private ShootingRange shootingRange;
    private StoreHouse storeHouse;
    private TrainingCenter trainingCenter;
    private WorkShop workShop;

    public Base(){}

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ClearingShop getClearingShop() {
        return clearingShop;
    }

    public void setClearingShop(ClearingShop clearingShop) {
        this.clearingShop = clearingShop;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public RestHouse getRestHouse() {
        return restHouse;
    }

    public void setRestHouse(RestHouse restHouse) {
        this.restHouse = restHouse;
    }

    public ShootingRange getShootingRange() {
        return shootingRange;
    }

    public void setShootingRange(ShootingRange shootingRange) {
        this.shootingRange = shootingRange;
    }

    public StoreHouse getStoreHouse() {
        return storeHouse;
    }

    public void setStoreHouse(StoreHouse storeHouse) {
        this.storeHouse = storeHouse;
    }

    public TrainingCenter getTrainingCenter() {
        return trainingCenter;
    }

    public void setTrainingCenter(TrainingCenter trainingCenter) {
        this.trainingCenter = trainingCenter;
    }

    public WorkShop getWorkShop() {
        return workShop;
    }

    public void setWorkShop(WorkShop workShop) {
        this.workShop = workShop;
    }
}

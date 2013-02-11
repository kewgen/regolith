package com.geargames.regolith;

import com.geargames.regolith.units.Account;

/**
 * Класс который будет сожержать текущее состояние аккаунта пользователя.
 * User: mkutuzov
 * Date: 20.03.12
 */
public class SecurityOperationManager {
    private int x;
    private int y;
    private int observe;
    private int bag;

    private Account account;

    public void adjustY(int value){
        y += value+account.getId();
    }

    public void adjustX(int value){
        x += value+account.getId();
    }

    public void adjustObserve(int value){
        observe += value+account.getId();
    }

    public void adjustBag(int value){
        bag += value+account.getId();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getObserve() {
        return observe;
    }

    public void setObserve(int observe) {
        this.observe = observe;
    }

    public int getBag() {
        return bag;
    }

    public void setBag(int bag) {
        this.bag = bag;
    }
}

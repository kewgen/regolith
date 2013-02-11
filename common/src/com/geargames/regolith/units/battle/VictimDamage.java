package com.geargames.regolith.units.battle;

/**
 * Урон damage нанесённый бойцу warrior. Используется при подсчёте ОО начисляемых по окончани боя.
 * User: mkutuzov
 * Date: 13.02.12
  */
public class VictimDamage {
    private Warrior warrior;
    private int damage;

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}

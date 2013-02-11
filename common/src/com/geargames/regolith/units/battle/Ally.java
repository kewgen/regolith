package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Human;

/**
 * Сущность которая представляет собой бойца дружественного отряда. Содержит поля необходимые для формирования общей
 * зоны видимости союзных войск.
 * User: mkutuzov
 * Date: 30.03.12
 */
public class Ally extends Human {
    private short x;
    private short y;
    private BattleGroup battleGroup;
    private short number;

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

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
}

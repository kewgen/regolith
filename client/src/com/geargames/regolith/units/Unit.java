package com.geargames.regolith.units;

import com.geargames.common.Graphics;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * Боец постоянно движется по игровой сетке координат(над которой движется экран), эта сущность, какраз, предназначена
 * для связи бойца с этой сеткой.
 * User: mkutuzov
 * Date: 24.02.12
 */
public class Unit {
    public static final int MOVING_OFFSET = 32;
    public static final int STANDING_SHOOTING_OFFSET = 16;
    public static final int SITTING_OFFSET = 24;
    public static final int SITTING_SHOOTING_OFFSET = 32;

    private int mapX;
    private int mapY;
    private Warrior warrior;

    /**
     * Вернуть координату на игровой карте по горизонтальной оси.
     * @return
     */
    public int getMapX() {
        return mapX;
    }

    public void setMapX(int mapX) {
        this.mapX = mapX;
    }

    /**
     * Вернуть координату на игровой карте по вертикальной оси.
     * @return
     */
    public int getMapY() {
        return mapY;
    }

    public void setMapY(int mapY) {
        this.mapY = mapY;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    /**
     * Рисуем бойца.
     * @param graphics
     */
    private void draw(Graphics graphics) {
        int offset = 0;
        if (warrior.isMoving()) {
            WeaponCategory category = warrior.getWeapon().getWeaponType().getCategory();


                        offset = Unit.MOVING_OFFSET;
        } else {
            if (warrior.isShooting()) {
                if (warrior.isSitting()) {
                    offset = Unit.SITTING_SHOOTING_OFFSET;
                } else {
                    offset = Unit.STANDING_SHOOTING_OFFSET;
                }
            } else {
                if (warrior.isSitting()) {
                    offset = Unit.SITTING_OFFSET;
                }
            }
        }
        offset += (warrior.getDirection().getNumber() + 1) % 8;
        //graphics.getRender().getUnit(warrior.getFrameId() + offset).draw(graphics, getMapX() - mapX, unit.getMapY() - mapY, null);
    }


}

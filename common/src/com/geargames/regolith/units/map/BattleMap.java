package com.geargames.regolith.units.map;

import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.battle.BattleType;

/**
 * Users: mkutuzov, abarakov
 * Date: 14.02.12
 * Игровая карта в 2d изометрии (псевдо 3d).
 * <p/>
 * /\
 * X /  \ Y
 * /    \
 */
public class BattleMap extends Entity {
    private String name;
    private BattleCell[][] cells;
    private ExitZone[] exits;
    private BattleType[] possibleBattleTypes;
    private byte[] content;

    public BattleType[] getPossibleBattleTypes() {
        return possibleBattleTypes;
    }

    public void setPossibleBattleTypes(BattleType[] possibleBattleTypes) {
        this.possibleBattleTypes = possibleBattleTypes;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExitZone[] getExits() {
        return exits;
    }

    public void setExits(ExitZone[] exits) {
        this.exits = exits;
    }

    // BattleCell[x][y]
    public BattleCell[][] getCells() {
        return cells;
    }

    public void setCells(BattleCell[][] cells) {
        this.cells = cells;
    }

    @Override
    public String toString() {
        String dimension;
        if (cells == null) {
            dimension = "null";
        } else if (cells.length == 0) {
            dimension = "0x0";
        } else {
            dimension = cells[0].length + "x" + cells.length; //todo: или наоборот
        }
        return super.toString() + "; name='" + getName() + "'; size=" + dimension;
    }

}

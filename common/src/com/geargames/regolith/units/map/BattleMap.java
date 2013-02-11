package com.geargames.regolith.units.map;

import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.battle.BattleType;

/**
 * User: mkutuzov
 * Date: 14.02.12
 * Игровая карта.
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

    public BattleCell[][] getCells() {
        return cells;
    }

    public void setCells(BattleCell[][] cells) {
        this.cells = cells;
    }
}

package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.Entity;

/**
 * @author Mikhail_Kutuzov
 *
 */
public class BattleType extends Entity {
    private String name;
    private byte scores;

    private int allianceAmount;
    private int allianceSize;
    private int groupSize;

    public BattleType() {
    }

    public byte getScores() {
        return scores;
    }

    public void setScores(byte scores) {
        this.scores = scores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAllianceAmount() {
        return allianceAmount;
    }

    public void setAllianceAmount(int allianceAmount) {
        this.allianceAmount = allianceAmount;
    }

    public int getAllianceSize() {
        return allianceSize;
    }

    public void setAllianceSize(int allianceSize) {
        this.allianceSize = allianceSize;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }
}

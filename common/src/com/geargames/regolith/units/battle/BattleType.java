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

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public byte getScores() {
        return scores;
    }

    public void setScores(byte scores) {
        this.scores = scores;
    }

    /**
     * Вернуть количество команд (союзов игроков) участвующих в битве.
     * @return
     */
    public int getAllianceAmount() {
        return allianceAmount;
    }

    public void setAllianceAmount(int allianceAmount) {
        this.allianceAmount = allianceAmount;
    }

    /**
     * Вернуть количество игроков участвующих в каждой команде во время битвы.
     * @return
     */
    //todo: AllianceSize -> PlayersPerAlliance
    public int getAllianceSize() {
        return allianceSize;
    }

    public void setAllianceSize(int allianceSize) {
        this.allianceSize = allianceSize;
    }

    /**
     * Вернуть количество бойцов (юнитов) сражающихся за каждого игрока в битве.
     * @return
     */
    //todo: GroupSize -> FighterPerPlayer, HumansPerPlayer
    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }
}

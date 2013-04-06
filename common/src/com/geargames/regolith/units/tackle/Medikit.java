package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.ElementTypes;
import com.geargames.regolith.units.Skill;

/**
 * User: mkutuzov
 * Date: 08.02.12
  */
public class Medikit extends Ammunition {
    private short value;
    private byte actionScores;
    private Skill minSkill;

    public Skill getMinSkill() {
        return minSkill;
    }

    public void setMinSkill(Skill minSkill) {
        this.minSkill = minSkill;
    }

    public byte getActionScores() {
        return actionScores;
    }

    public void setActionScores(byte actionScores) {
        this.actionScores = actionScores;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public int getType() {
        return TackleType.MEDIKIT;
    }

    @Override
    public short getElementType() {
        return ElementTypes.MEDIKIT;
    }
}

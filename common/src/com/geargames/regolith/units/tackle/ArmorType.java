package com.geargames.regolith.units.tackle;

import com.geargames.regolith.units.CellElementTypes;

/**
 * Позиция из каталога бони. Представляет базовые характеристи - тип брони.
 * User: mkutuzov
 * Date: 03.02.12
 * Time: 22:22
 */
public class ArmorType extends Tackle {
    private byte bodyParticle;
    private short speedBonus;
    private short visibilityBonus;
    private short strengthBonus;
    private short craftinessBonus;
    private short regenerationBonus;
    private short marksmanshipBonus;
    private short baseFirmness;
    private byte armor;

    public byte getArmor() {
        return armor;
    }

    public void setArmor(byte armor) {
        this.armor = armor;
    }

    public short getBaseFirmness() {
        return baseFirmness;
    }

    public void setBaseFirmness(short baseFirmness) {
        this.baseFirmness = baseFirmness;
    }

    public byte getBodyParticle() {
        return bodyParticle;
    }

    public void setBodyParticle(byte bodyParticle) {
        this.bodyParticle = bodyParticle;
    }

    public short getSpeedBonus() {
        return speedBonus;
    }

    public void setSpeedBonus(short speedBonus) {
        this.speedBonus = speedBonus;
    }

    public short getVisibilityBonus() {
        return visibilityBonus;
    }

    public void setVisibilityBonus(short visibilityBonus) {
        this.visibilityBonus = visibilityBonus;
    }

    public short getStrengthBonus() {
        return strengthBonus;
    }

    public void setStrengthBonus(short strengthBonus) {
        this.strengthBonus = strengthBonus;
    }

    public short getCraftinessBonus() {
        return craftinessBonus;
    }

    public void setCraftinessBonus(short craftinessBonus) {
        this.craftinessBonus = craftinessBonus;
    }

    public short getRegenerationBonus() {
        return regenerationBonus;
    }

    public void setRegenerationBonus(short regenerationBonus) {
        this.regenerationBonus = regenerationBonus;
    }

    public short getMarksmanshipBonus() {
        return marksmanshipBonus;
    }

    public void setMarksmanshipBonus(short marksmanshipBonus) {
        this.marksmanshipBonus = marksmanshipBonus;
    }

    public int getType() {
        return TackleType.TYPE;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.NULL;
    }
}

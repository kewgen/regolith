package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.CellElementLayers;
import com.geargames.regolith.units.CellElementTypes;
import com.geargames.regolith.units.dictionaries.MagazineCollection;
import com.geargames.regolith.units.dictionaries.MedikitCollection;
import com.geargames.regolith.units.dictionaries.StateTackleCollection;
import com.geargames.regolith.units.tackle.*;

/**
 * Коробка на поле боя, содержит в себе различные предметы и кучи зарядов оружия.
 * User: mkutuzov
 * Date: 29.03.12
 */
public class Box extends CellElement {
    public static final byte TACKLE = 0;
    public static final byte MEDIKIT = 1;
    public static final byte MAGAZINE = 2;

    private StateTackleCollection tackles;
    private MedikitCollection medikits;
    private MagazineCollection magazines;
    private int frameId;

    public MedikitCollection getMedikits() {
        return medikits;
    }

    public void setMedikits(MedikitCollection medikits) {
        this.medikits = medikits;
    }

    public MagazineCollection getMagazines() {
        return magazines;
    }

    public void setMagazines(MagazineCollection magazines) {
        this.magazines = magazines;
    }

    public StateTackleCollection getTackles() {
        return tackles;
    }

    public void setTackles(StateTackleCollection tackles) {
        this.tackles = tackles;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    @Override
    public int getFrameId() {
        return frameId;
    }

    @Override
    public boolean isAbleToLookThrough() {
        return true;
    }

    @Override
    public boolean isAbleToWalkThrough() {
        return false;
    }

    @Override
    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return true;
    }

    @Override
    public boolean isHalfLong() {
        return true;
    }
    @Override
    public boolean isBarrier() {
        return false;
    }

    @Override
    public short getElementType() {
        return CellElementTypes.BOX;
    }

    @Override
    public byte getLayer() {
        return CellElementLayers.DYNAMIC;
    }

}

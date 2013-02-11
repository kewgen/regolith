package com.geargames.regolith.units.battle;

import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.dictionaries.MagazineCollection;
import com.geargames.regolith.units.dictionaries.MedikitCollection;
import com.geargames.regolith.units.dictionaries.StateTackleCollection;
import com.geargames.regolith.units.tackle.*;

/**
 * Коробка на поле боя, содержит в себе различные предметы и кучи зарядов оружия.
 * User: mkutuzov
 * Date: 29.03.12
 */
public class Box extends Element {
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

    public int getFrameId() {
        return frameId;
    }

    public boolean isAbleToLookThrough() {
        return true;
    }

    public boolean isAbleToWalkThrough() {
        return false;
    }

    public boolean isAbleToShootThrough(WeaponCategory weaponCategory) {
        return true;
    }

    public boolean isHalfLong() {
        return true;
    }
}

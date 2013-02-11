package com.geargames.regolith.units.tackle;

import com.geargames.regolith.BattleConfiguration;

/**
 * User: mkutuzov
 * Date: 08.02.12
 */
public class Armor extends StateTackle {
    private ArmorType armorType;

    public int getFrameId() {
        return armorType.getFrameId();
    }

    public String getName() {
        return armorType.getName();
    }

    public short getWeight() {
        return armorType.getWeight();
    }

    public ArmorType getArmorType() {
        return armorType;
    }

    public void setArmorType(ArmorType armorType) {
        this.armorType = armorType;
    }

    /**
     * Здесь меняем состояние брони в случае удара по ней с силой = damage едениц.
     * @param damage
     */
    public void onHit(int damage, BattleConfiguration battleConfiguration){
       setState((short)(getState() - damage/ battleConfiguration.getArmorSpoiling()));
    }

    public int getType() {
        return TackleType.ARMOR;
    }
}

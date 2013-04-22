package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BodyParticles;
import com.geargames.regolith.units.CellElement;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mkutuzov
 * Date: 24.07.12
 */
public class ServerTackleGround2BodyRequest extends ServerGround2WarriorRequest {
    private BaseConfiguration baseConfiguration;

    public ServerTackleGround2BodyRequest(ServerBattle serverBattle, BaseConfiguration baseConfiguration) {
        super(serverBattle, Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BODY);
        this.baseConfiguration = baseConfiguration;
    }

    @Override
    protected boolean putStateTackle(CellElement stateTackle, BattleCell cell, Warrior warrior) {
        if (stateTackle instanceof Weapon) {
            Weapon weapon = (Weapon) stateTackle;
            if (warrior.getWeapon() != null) {
                if (!WarriorHelper.putInToBag(warrior, warrior.getWeapon(), baseConfiguration)) {
                    return false;
                }
            }
            warrior.setWeapon(weapon);
        } else {
            Armor armor = (Armor) stateTackle;
            switch (armor.getArmorType().getBodyParticle()) {
                case BodyParticles.HEAD:
                    if (warrior.getHeadArmor() != null) {
                        if (!WarriorHelper.putInToBag(warrior, warrior.getWeapon(), baseConfiguration)) {
                            return false;
                        }
                        warrior.setHeadArmor(armor);
                    }
                    break;
                case BodyParticles.LEGS:
                    if (warrior.getLegsArmor() != null) {
                        if (!WarriorHelper.putInToBag(warrior, warrior.getWeapon(), baseConfiguration)) {
                            return false;
                        }
                        warrior.setLegsArmor(armor);
                    }
                    break;
                case BodyParticles.TORSO:
                    if (warrior.getTorsoArmor() != null) {
                        if (!WarriorHelper.putInToBag(warrior, warrior.getWeapon(), baseConfiguration)) {
                            return false;
                        }
                        warrior.setTorsoArmor(armor);
                    }
                    break;
                default:
                    return false;
            }
        }
        cell.removeElement(stateTackle);
        return true;
    }
}

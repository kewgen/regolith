package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BodyParticles;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerStateTackleCollection;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mikhail v. kutuzov
 * Date: 16.08.12
 * Time: 14:34
 */
public class ServerTackleBox2BodyRequest extends ServerBox2WarriorRequest {
    private BaseConfiguration baseConfiguration;

    public ServerTackleBox2BodyRequest(ServerBattle serverBattle, BaseConfiguration baseConfiguration) {
        super(Packets.TAKE_TACKLE_FROM_BOX_PUT_INTO_BODY, serverBattle);
        this.baseConfiguration = baseConfiguration;
    }

    @Override
    protected boolean putElement(int elementId, Box box, Warrior warrior) throws RegolithException{
        StateTackle tackle = null;

        for (StateTackle sTackle : ((ServerStateTackleCollection) box.getTackles()).getTackles()) {
            if (sTackle.getId() == elementId) {
                tackle = sTackle;
                break;
            }
        }

        if (tackle == null) {
            throw new RegolithException("There is no tackle [" + elementId + "] in the box");
        }

        if (tackle instanceof Weapon) {
            if (warrior.getWeapon() != null) {
                if (WarriorHelper.putInToBag(warrior, warrior.getWeapon(), baseConfiguration)) {
                    return false;
                }
            }
            warrior.setWeapon((Weapon) tackle);
        } else if (tackle instanceof Armor) {
            Armor armor = (Armor) tackle;
            switch (armor.getArmorType().getBodyParticle()) {
                case BodyParticles.HEAD:
                    if (warrior.getHeadArmor() != null) {
                        if (WarriorHelper.putInToBag(warrior, warrior.getWeapon(), baseConfiguration)) {
                            return false;
                        }
                        warrior.setHeadArmor((Armor) tackle);
                    }
                    break;
                case BodyParticles.TORSO:
                    if (warrior.getTorsoArmor() != null) {
                        if (WarriorHelper.putInToBag(warrior, warrior.getWeapon(), baseConfiguration)) {
                            return false;
                        }
                        warrior.setTorsoArmor((Armor) tackle);
                    }
                    break;
                case BodyParticles.LEGS:
                    if (warrior.getLegsArmor() != null) {
                        if (WarriorHelper.putInToBag(warrior, warrior.getWeapon(), baseConfiguration)) {
                            return false;
                        }
                        warrior.setLegsArmor((Armor) tackle);
                    }
                    break;
            }
        }
        ((ServerStateTackleCollection)box.getTackles()).getTackles().remove(tackle);
        return false;
    }
}

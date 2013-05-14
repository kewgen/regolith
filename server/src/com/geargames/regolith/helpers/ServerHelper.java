package com.geargames.regolith.helpers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.units.*;
import com.geargames.regolith.units.battle.Human;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ServerAmmunitionPacketCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * User: mikhail v. kutuzov
 * Date: 11.12.12
 * Time: 12:31
 */
public class ServerHelper {

    public static AmmunitionBag createAmmunitionBag(BaseConfiguration baseConfiguration) {
        AmmunitionBag bag = new AmmunitionBag();
        ServerAmmunitionPacketCollection packets = new ServerAmmunitionPacketCollection();
        packets.setPackets(new ArrayList<AmmunitionPacket>(baseConfiguration.getPocketsAmount()));
        bag.setPackets(packets);
        packets = new ServerAmmunitionPacketCollection();
        packets.setPackets(new LinkedList<AmmunitionPacket>());
        bag.setReserve(packets);
        bag.setWeight(0);
        return bag;
    }

    public static Warrior createWarrior(BaseConfiguration configuration, Bag bag, AmmunitionBag ammunitionBag, String name, int frameId) {
        Warrior warrior = new Warrior();
        warrior.setName(name);
        warrior.setFrameId(frameId);
        warrior.setBirthDate(new Date());
        int length = configuration.getWeaponCategories().size();
        warrior.setSkillScores(new short[length]);
        warrior.setBag(bag);
        warrior.setSkills(new Skill[length]);
        warrior.setAmmunitionBag(ammunitionBag);
        warrior.setVictimsDamages(new Hashtable());
        return warrior;
    }

}

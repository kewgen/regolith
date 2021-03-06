package com.geargames.regolith.helpers;

import com.geargames.common.network.Network;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.answers.ClientChangeActiveAllianceAnswer;
import com.geargames.regolith.units.AmmunitionBag;
import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.base.StoreHouse;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientAmmunitionPacketCollection;
import com.geargames.regolith.units.dictionaries.ClientStateTackleCollection;
import com.geargames.regolith.units.dictionaries.ClientWeaponCategoryCollection;
import com.geargames.regolith.units.dictionaries.WeaponCategoryCollection;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Date: 01.10.12
 * Time: 22:12
 *
 * Реализация хелпера основана на том, что все действия происходят из одного потока.
 */
public class ClientHelper {
    private static ClientWeaponCategoryCollection categories = new ClientWeaponCategoryCollection(6);

    public static StoreHouse createStoreHouse(BaseConfiguration baseConfiguration){
        StoreHouse house = new StoreHouse();
        Bag bag = new Bag();
        bag.setTackles(new ClientStateTackleCollection(new Vector()));
        bag.setWeight((short)0);
        AmmunitionBag ammunitionBag = ClientHelper.createAmmunitionBag(baseConfiguration);
        ammunitionBag.setWeight(0);
        house.setBag(bag);
        house.setAmmunitionBag(ammunitionBag);

        return house;
    }

    public static AmmunitionBag createAmmunitionBag(BaseConfiguration baseConfiguration) {
        AmmunitionBag bag = new AmmunitionBag();
        ClientAmmunitionPacketCollection packets = new ClientAmmunitionPacketCollection();
        packets.setPackets(new Vector(baseConfiguration.getPocketsAmount()));
        bag.setPackets(packets);
        packets = new ClientAmmunitionPacketCollection();
        packets.setPackets(new Vector(10));
        bag.setReserve(packets);
        bag.setWeight(0);
        return bag;
    }


    /**
     * Вернуть отсортированный список разновидностей оружия.
     * @param warrior
     * @return
     */
    public static ClientWeaponCategoryCollection getOrderedWeaponCategories(Warrior warrior) {
        short[] scores = warrior.getSkillScores();
        int length = scores.length;
        categories.getWeaponCategories().clear();
        WeaponCategoryCollection baseCategories = ClientConfigurationFactory.getConfiguration().getBaseConfiguration().getWeaponCategories();
        for (int i = 0; i < length - 1; i++) {
            int rank = 0;
            for (int j = 0; j < length; j++) {
                if(i != j){
                    if(scores[i] > scores[j]){
                        rank++;
                    }
                }
            }
            categories.insert(baseCategories.get(i),rank);
        }
        return categories;
    }


}

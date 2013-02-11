package com.geargames.regolith.serializers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Human;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.ExitZone;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.Weapon;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 16.04.12
 */
public class BattleDeserializer {

    public static void deserializeAlly(Ally ally, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        deserializeHuman(ally, buffer, baseConfiguration);
        ally.setX(SimpleDeserializer.deserializeShort(buffer));
        ally.setY(SimpleDeserializer.deserializeShort(buffer));
        ally.setNumber(SimpleDeserializer.deserializeShort(buffer));
    }

    private static void deserializeHuman(Human human, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        human.setId(SimpleDeserializer.deserializeInt(buffer));
        human.setName(SimpleDeserializer.deserializeString(buffer));
        int rankId = SimpleDeserializer.deserializeInt(buffer);
        human.setRank(BaseConfigurationHelper.findRankById(rankId, baseConfiguration));
        human.setFrameId(SimpleDeserializer.deserializeInt(buffer));
        human.setHealth(SimpleDeserializer.deserializeInt(buffer));
        if (buffer.get() == SimpleSerializer.YES) {
            Armor armor = new Armor();
            human.setHeadArmor(TackleDeserializer.deSerialize(armor, buffer, baseConfiguration));
        }
        if (buffer.get() == SimpleSerializer.YES) {
            Armor armor = new Armor();
            human.setTorsoArmor(TackleDeserializer.deSerialize(armor, buffer, baseConfiguration));
        }
        if (buffer.get() == SimpleSerializer.YES) {
            Armor armor = new Armor();
            human.setLegsArmor(TackleDeserializer.deSerialize(armor, buffer, baseConfiguration));
        }
        Weapon weapon = new Weapon();
        human.setWeapon(TackleDeserializer.deSerialize(weapon, buffer, baseConfiguration));
    }

    private static void deserialize(ExitZone exitZone, MicroByteBuffer buffer) {
        exitZone.setId(SimpleDeserializer.deserializeInt(buffer));
        exitZone.setX(SimpleDeserializer.deserializeShort(buffer));
        exitZone.setY(SimpleDeserializer.deserializeShort(buffer));
        exitZone.setxRadius(buffer.get());
        exitZone.setyRadius(buffer.get());
    }

    private static void deserializeAllies(BattleAlliance battleAlliance, MicroByteBuffer buffer,
                                          BaseConfiguration configuration, Battle battle, Account account) {
        battleAlliance.setId(SimpleDeserializer.deserializeInt(buffer));
        battleAlliance.setNumber(buffer.get());
        ExitZone exit = new ExitZone();
        battleAlliance.setExit(exit);
        deserialize(exit, buffer);
        BattleGroupCollection battleGroups = new ClientBattleGroupCollection();
        battleAlliance.setAllies(battleGroups);
        for (int i = 0; i < battle.getBattleType().getAllianceSize(); i++) {
            BattleGroup battleGroup = new BattleGroup();
            battleGroups.add(battleGroup);
            battleGroup.setAlliance(battleAlliance);

            battleGroup.setWarriors(new ClientWarriorCollection(new Vector()));
            battleGroup.setId(SimpleDeserializer.deserializeInt(buffer));
            if (buffer.get() == SimpleSerializer.NO) {
                for (int j = 0; j < battle.getBattleType().getGroupSize(); j++) {
                    Warrior ally = new Warrior();
                    battleGroup.getWarriors().add(ally);
                    deserializeAlly(ally, buffer, configuration);
                    ally.setBattleGroup(battleGroup);
                }
            } else {
                WarriorCollection mineWarriors = account.getWarriors();
                int mineLength = mineWarriors.size();
                for (int j = 0; j < battle.getBattleType().getGroupSize(); j++) {
                    int warriorId = SimpleDeserializer.deserializeInt(buffer);
                    Warrior warrior = null;
                    for (int k = 0; k < mineLength; k++) {
                        if (mineWarriors.get(k).getId() == warriorId) {
                            warrior = mineWarriors.get(k);
                            break;
                        }
                    }
                    if (warrior == null) {
                        throw new IllegalArgumentException();
                    }
                    warrior.setBattleGroup(battleGroup);
                    battleGroup.getWarriors().add(warrior);
                    warrior.setBattleGroup(battleGroup);
                }
            }
        }
    }

    private static void deserializeEnemies(BattleAlliance battleAlliance, MicroByteBuffer buffer,
                                           BaseConfiguration configuration, Battle battle, Account account) {
        battleAlliance.setId(SimpleDeserializer.deserializeInt(buffer));
        battleAlliance.setNumber(buffer.get());
        ExitZone exit = new ExitZone();
        battleAlliance.setExit(exit);
        deserialize(exit, buffer);
        int length = battle.getBattleType().getAllianceSize();
        BattleGroupCollection battleGroups = new ClientBattleGroupCollection();
        battleAlliance.setAllies(battleGroups);
        for (int i = 0; i < length; i++) {
            BattleGroup battleGroup = new BattleGroup();
            battleGroups.add(battleGroup);
            battleGroup.setAlliance(battleAlliance);
            battleGroup.setWarriors(new ClientWarriorCollection(new Vector()));
            battleGroup.setId(SimpleDeserializer.deserializeInt(buffer));
            for (int j = 0; j < battle.getBattleType().getGroupSize(); j++) {
                Warrior warrior = new Warrior();
                deserializeHuman(warrior, buffer, configuration);
                battleGroup.getWarriors().add(warrior);
                warrior.setBattleGroup(battleGroup);
            }
        }
    }

    public static Battle deserializeBattle(MicroByteBuffer buffer, BaseConfiguration baseConfiguration, Account account) {
        Battle battle = new Battle();
        battle.setId(SimpleDeserializer.deserializeInt(buffer));
        battle.setName(SimpleDeserializer.deserializeString(buffer));
        battle.setBattleType(BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
        buffer.mark();
        if (SimpleDeserializer.deserializeInt(buffer) != -1) {
            buffer.reset();
            byte length = buffer.get();
            BattleAlliance[] battleAlliances = new BattleAlliance[length];
            for (int i = 0; i < length; i++) {
                battleAlliances[i] = new BattleAlliance();
                if (buffer.get() == SimpleSerializer.ALLY) {
                    deserializeAllies(battleAlliances[i], buffer, baseConfiguration, battle, account);
                } else {
                    deserializeEnemies(battleAlliances[i], buffer, baseConfiguration, battle, account);
                }
            }
            battle.setAlliances(battleAlliances);
        }
        battle.setMap(BattleMapDeserializer.deserializeBattleMap(buffer, baseConfiguration, battle, account));
        BattleMap map = battle.getMap();
        if (map != null) {
            int amount = battle.getAlliances().length;
            ExitZone[] exitZones = new ExitZone[amount];
            map.setExits(exitZones);
            for (int i = 0; i < amount; i++) {
                exitZones[i] = battle.getAlliances()[i].getExit();
            }
        }
        return battle;
    }
}

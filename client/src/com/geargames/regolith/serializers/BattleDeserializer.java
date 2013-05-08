package com.geargames.regolith.serializers;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Human;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.ClientWarriorElement;
import com.geargames.regolith.units.map.ExitZone;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.Weapon;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 16.04.12
 */
public class BattleDeserializer {

    public static void deserializeWarrior(Warrior warrior, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        deserializeHuman(warrior, buffer, baseConfiguration);
        warrior.setCellX(SimpleDeserializer.deserializeShort(buffer));
        warrior.setCellY(SimpleDeserializer.deserializeShort(buffer));
    }

    public static void deserializeHuman(Human human, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        human.setId(SimpleDeserializer.deserializeInt(buffer));
        human.setName(SimpleDeserializer.deserializeString(buffer));
        human.setNumber(SimpleDeserializer.deserializeShort(buffer));
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
        battleAlliance.setBattle(battle);
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
                // Сериализация союзных бойцов
                for (int j = 0; j < battle.getBattleType().getGroupSize(); j++) {
                    ClientWarriorElement ally = new ClientWarriorElement();
                    ally.setMembershipType(Human.ALLY);
                    battleGroup.getWarriors().add(ally);
                    deserializeWarrior(ally, buffer, configuration);
                    ally.setBattleGroup(battleGroup);
                }
            } else {
                // Сериализация своих бойцов
                WarriorCollection mineWarriors = account.getWarriors();
                int mineLength = mineWarriors.size();
                for (int j = 0; j < battle.getBattleType().getGroupSize(); j++) {
                    int warriorId = SimpleDeserializer.deserializeInt(buffer);
                    short number = SimpleDeserializer.deserializeShort(buffer);
                    int direction = SimpleDeserializer.deserializeInt(buffer);
                    Warrior warrior = null;
                    for (int k = 0; k < mineLength; k++) {
                        if (mineWarriors.get(k).getId() == warriorId) {
                            warrior = mineWarriors.get(k);
                            warrior.setNumber(number);
                            warrior.setDirection(Direction.getByNumber(direction));
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
        battleAlliance.setBattle(battle);
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
                ClientWarriorElement warrior = new ClientWarriorElement();
                warrior.setMembershipType(Human.ENEMY);
                deserializeHuman(warrior, buffer, configuration);
                battleGroup.getWarriors().add(warrior);
                warrior.setBattleGroup(battleGroup);
            }
        }
    }

    public static void deserializeBattle(MicroByteBuffer buffer, BaseConfiguration baseConfiguration, Account account, Battle battle) throws RegolithException {
        battle.setId(SimpleDeserializer.deserializeInt(buffer));
        battle.setName(SimpleDeserializer.deserializeString(buffer));
        battle.setBattleType(BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
        byte length = buffer.get();
        if (length > 0) {
            BattleAlliance[] battleAlliances = new BattleAlliance[length];
            for (int i = 0; i < length; i++) {
                BattleAlliance alliance = new BattleAlliance();
                battleAlliances[i] = alliance;
                alliance.setNumber((byte) i);
                short kind = SimpleDeserializer.deserializeShort(buffer);
                if (kind == SerializeHelper.ALLY) {
                    deserializeAllies(alliance, buffer, baseConfiguration, battle, account);
                } else if (kind == SerializeHelper.ENEMY) {
                    deserializeEnemies(alliance, buffer, baseConfiguration, battle, account);
                } else {
                    Debug.error("Failed to recognize the membership of the alliance (kind = " + kind + ")");
                }
            }
            battle.setAlliances(battleAlliances);
        } else {
            battle.setAlliances(null);
        }
        BattleMap map = BattleMapDeserializer.deserializeBattleMap(buffer, baseConfiguration, battle, account);
        battle.setMap(map);
        if (map != null) {
            int amount = battle.getAlliances().length;
            ExitZone[] exitZones = new ExitZone[amount];
            map.setExits(exitZones);
            for (int i = 0; i < amount; i++) {
                exitZones[i] = battle.getAlliances()[i].getExit();
            }
        }
        Account author = new Account();
        battle.setAuthor(author);
        author.setId(SimpleDeserializer.deserializeInt(buffer));
        author.setName(SimpleDeserializer.deserializeString(buffer));
        author.setFrameId(SimpleDeserializer.deserializeInt(buffer));
    }

    public static Battle deserializeBattle(MicroByteBuffer buffer, BaseConfiguration baseConfiguration, Account account) throws RegolithException {
        Battle battle = new Battle();
        deserializeBattle(buffer, baseConfiguration, account, battle);
        return battle;
    }

}

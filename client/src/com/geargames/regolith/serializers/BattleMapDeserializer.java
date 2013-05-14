package com.geargames.regolith.serializers;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.*;
import com.geargames.regolith.units.tackle.*;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 12.04.12
 */
public class BattleMapDeserializer {

    private static void deserializeBox(Box box, MicroByteBuffer buffer, BaseConfiguration configuration) {
        box.setId(SimpleDeserializer.deserializeInt(buffer));
        box.setFrameId(SimpleDeserializer.deserializeInt(buffer));
        byte length = buffer.get();
        StateTackleCollection tackles = new ClientStateTackleCollection(new Vector());
        for (int i = 0; i < length; i++) {
            short typeId = SimpleDeserializer.deserializeShort(buffer);
            if (typeId == SerializeHelper.ARMOR) {
                Armor armor = new Armor();
                TackleDeserializer.deSerialize(armor, buffer, configuration);
                tackles.add(armor);
            } else if (typeId == SerializeHelper.WEAPON) {
                Weapon weapon = new Weapon();
                TackleDeserializer.deSerialize(weapon, buffer, configuration);
                tackles.add(weapon);
            }
        }
        box.setTackles(tackles);
        length = buffer.get();
        MedikitCollection medikits = new ClientMedikitCollection();
        for (int i = 0; i < length; i++) {
            medikits.add(BaseConfigurationHelper.findMedikitById(SimpleDeserializer.deserializeInt(buffer), configuration));
        }
        box.setMedikits(medikits);
        length = buffer.get();
        MagazineCollection magazines = new ClientMagazineCollection();
        for (int i = 0; i < length; i++) {
            Magazine magazine = new Magazine();
            deserializeMagazine(magazine, buffer, configuration);
            magazines.add(magazine);
        }
        box.setMagazines(magazines);
    }

    private static Magazine deserializeMagazine(Magazine magazine, MicroByteBuffer buffer, BaseConfiguration configuration) {
        magazine.setId(SimpleDeserializer.deserializeInt(buffer));
        magazine.setProjectile(BaseConfigurationHelper.findProjectileById(SimpleDeserializer.deserializeInt(buffer), configuration));
        magazine.setCount(SimpleDeserializer.deserializeShort(buffer));
        return magazine;
    }

    public static BattleMap deserializeBattleMap(MicroByteBuffer buffer, BaseConfiguration baseConfiguration,
                                                 Battle battle, Account account) throws RegolithException {
        int mapId = SimpleDeserializer.deserializeInt(buffer);
        if (mapId == SerializeHelper.NULL_REFERENCE) {
            Debug.critical("Deserialized null map reference");
            return null;
        }
        byte length = buffer.get();
        BattleType[] types = new BattleType[length];
        for (int i = 0; i < length; i++) {
            int battleTypeId = SimpleDeserializer.deserializeInt(buffer);
            types[i] = BaseConfigurationHelper.findBattleTypeById(battleTypeId, baseConfiguration);
        }

        String name = SimpleDeserializer.deserializeString(buffer);
        short mapSize = SimpleDeserializer.deserializeShort(buffer);
        int last = SimpleDeserializer.deserializeInt(buffer) + buffer.getPosition();
        BattleMap battleMap = ClientBattleHelper.createBattleMap(mapSize);
        battleMap.setPossibleBattleTypes(types);
        battleMap.setName(name);
        BattleCell[][] cells = battleMap.getCells();
        battleMap.setId(mapId);

        while (buffer.getPosition() != last) {
            short cellX = SimpleDeserializer.deserializeShort(buffer);
            short cellY = SimpleDeserializer.deserializeShort(buffer);
            byte size = buffer.get();
            int direction;
            for (int k = 0; k < size; k++) {
                short typeId = SimpleDeserializer.deserializeShort(buffer);
                switch (typeId) {
                    case SerializeHelper.WARRIOR:
                        int warriorId = SimpleDeserializer.deserializeInt(buffer);
                        direction = SimpleDeserializer.deserializeInt(buffer);
                        Warrior unit = ClientBattleHelper.findWarrior(account, warriorId);
                        WarriorHelper.putWarriorIntoMap(cells, unit, cellX, cellY);
                        unit.setDirection(Direction.getByNumber(direction));
                        break;
                    case SerializeHelper.ALLY:
                        int allyId = SimpleDeserializer.deserializeInt(buffer);
                        direction = SimpleDeserializer.deserializeInt(buffer);
                        boolean found = false;
                        BattleAlliance alliance = ClientBattleHelper.findBattleAlliance(battle, account);
                        BattleGroupCollection groups = alliance.getAllies();
                        for (int i = 0; i < groups.size(); i++) {
                            BattleGroup group = groups.get(i);
                            if (group.getAccount() != account) {
                                int len = group.getWarriors().size();
                                for (int j = 0; j < len; j++) {
                                    Warrior warrior = group.getWarriors().get(j);
                                    if (warrior.getId() == allyId) {
                                        found = true;
                                        WarriorHelper.putWarriorIntoMap(cells, warrior, cellX, cellY);
                                        warrior.setDirection(Direction.getByNumber(direction));
                                        break;
                                    }
                                }
                                if (found) {
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            Debug.critical("Deserialized warrior does not found (allyId = " + allyId + ")");
                        }
                        break;
                    case SerializeHelper.BOX:
                        ClientBox box = new ClientBox();
                        cells[cellX][cellY].addElement(box);
                        deserializeBox(box, buffer, baseConfiguration);
                        break;
                    case SerializeHelper.MAGAZINE:
                        Magazine magazine = new Magazine();
                        cells[cellX][cellY].addElement(magazine);
                        deserializeMagazine(magazine, buffer, baseConfiguration);
                        break;
                    case SerializeHelper.BARRIER:
                        cells[cellX][cellY].addElement(BaseConfigurationHelper.findBarrierById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
                        break;
                    case SerializeHelper.ARMOR:
                        Armor armor = new Armor();
                        cells[cellX][cellY].addElement(armor);
                        TackleDeserializer.deSerialize(armor, buffer, baseConfiguration);
                        break;
                    case SerializeHelper.WEAPON:
                        Weapon weapon = new Weapon();
                        cells[cellX][cellY].addElement(weapon);
                        TackleDeserializer.deSerialize(weapon, buffer, baseConfiguration);
                        break;
                    case SerializeHelper.MEDIKIT:
                        Medikit medikit = new Medikit();
                        cells[cellX][cellY].addElement(medikit);
                        TackleDeserializer.deserializeMedikit(medikit, buffer, baseConfiguration);
                        break;
                    default:
                        throw new IllegalArgumentException("This map element is not recognized (type identifier = " + typeId + ")");
                }
            }
        }
        return battleMap;
    }

    public static BattleMap deserializeLightBattleMap(MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        BattleMap battleMap = new BattleMap();
        battleMap.setId(SimpleDeserializer.deserializeInt(buffer));
        byte amount = buffer.get();
        BattleType[] battleTypes = new BattleType[amount];
        battleMap.setPossibleBattleTypes(battleTypes);
        for (int j = 0; j < amount; j++) {
            battleTypes[j] = BaseConfigurationHelper.findBattleTypeById(
                    SimpleDeserializer.deserializeInt(buffer), baseConfiguration);
        }
        battleMap.setName(SimpleDeserializer.deserializeString(buffer));
        return battleMap;
    }

}
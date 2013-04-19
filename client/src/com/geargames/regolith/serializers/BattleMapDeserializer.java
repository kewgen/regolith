package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.dictionaries.*;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
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
            if (typeId == SerializeHelper.findTypeId("Armor")) {
                Armor armor = new Armor();
                TackleDeserializer.deSerialize(armor, buffer, configuration);
                tackles.add(armor);
            } else if (typeId == SerializeHelper.findTypeId("Weapon")) {
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
            magazines.add(deserializeMagazine(buffer, configuration));
        }
        box.setMagazines(magazines);
    }

    private static Magazine deserializeMagazine(MicroByteBuffer buffer, BaseConfiguration configuration) {
        Magazine magazine = new Magazine();
        magazine.setId(SimpleDeserializer.deserializeInt(buffer));
        magazine.setProjectile(BaseConfigurationHelper.findProjectileById(SimpleDeserializer.deserializeInt(buffer), configuration));
        magazine.setCount(SimpleDeserializer.deserializeShort(buffer));
        return magazine;
    }

    public static BattleMap deserializeBattleMap(MicroByteBuffer buffer, BaseConfiguration baseConfiguration, Battle battle, Account account) {
        int id = SimpleDeserializer.deserializeInt(buffer);
        if (id == SerializeHelper.NULL_REFERENCE) {
            return null;
        }
        int length = buffer.get();
        BattleType[] types = new BattleType[length];
        for(int i = 0; i < length; i++){
           types[i] = BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration);
        }

        String name = SimpleDeserializer.deserializeString(buffer);
        length = SimpleDeserializer.deserializeShort(buffer);
        int last = SimpleDeserializer.deserializeInt(buffer) + buffer.getPosition();
        BattleMap battleMap = ClientBattleHelper.createBattleMap(length);
        battleMap.setPossibleBattleTypes(types);
        battleMap.setName(name);
        BattleCell[][] cells = battleMap.getCells();
        battleMap.setId(id);
        while (buffer.getPosition() != last) {
            short x = SimpleDeserializer.deserializeShort(buffer);
            short y = SimpleDeserializer.deserializeShort(buffer);
            short typeId = SimpleDeserializer.deserializeShort(buffer);
            if (typeId == SerializeHelper.findTypeId("Warrior")) {
                int warriorId = SimpleDeserializer.deserializeInt(buffer);
                WarriorCollection warriors = account.getWarriors();
                for (int i = 0; i < warriors.size(); i++) {
                    if (warriorId == warriors.get(i).getId()) {
                        WarriorHelper.putWarriorIntoMap(warriors.get(i), battleMap, x, y);
                        break;
                    }
                }
            } else if (typeId == SerializeHelper.findTypeId("Box")) {
                Box box = new Box();
                cells[x][y].setElement(box);
                deserializeBox(box, buffer, baseConfiguration);
            } else if (typeId == SerializeHelper.findTypeId("Magazine")) {
                cells[x][y].setElement(deserializeMagazine(buffer, baseConfiguration));
            } else if (typeId == SerializeHelper.findTypeId("Barrier")) {
                cells[x][y].setElement(BaseConfigurationHelper.findBarrierById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
            } else if (typeId == SerializeHelper.findTypeId("Armor")) {
                Armor armor = new Armor();
                cells[x][y].setElement(armor);
                TackleDeserializer.deSerialize(armor, buffer, baseConfiguration);
            } else if (typeId == SerializeHelper.findTypeId("Weapon")) {
                Weapon weapon = new Weapon();
                cells[x][y].setElement(weapon);
                TackleDeserializer.deSerialize(weapon, buffer, baseConfiguration);
            } else if (typeId == SerializeHelper.findTypeId("Medikit")) {
                Medikit medikit = new Medikit();
                cells[x][y].setElement(medikit);
                TackleDeserializer.deserializeMedikit(medikit, buffer, baseConfiguration);
            } else if (typeId == SerializeHelper.findTypeId("Ally")) {
                int allyId = SimpleDeserializer.deserializeInt(buffer);
                BattleAlliance[] alliances = battle.getAlliances();
                int alliancesLength = alliances.length;
                for (int i = 0; i < alliancesLength; i++) {
                    for (int j = 0; j < battle.getBattleType().getAllianceSize(); j++) {
                        if (alliances[i].getAllies().get(j).getWarriors().get(0).getBattleGroup().getAccount() == account) {
                            BattleGroupCollection groups = alliances[i].getAllies();
                            for (int k = 0; k < groups.size(); k++) {
                                WarriorCollection warriors = groups.get(k).getWarriors();
                                int len = warriors.size();
                                boolean found = false;
                                for (int n = 0; n < len; n++) {
                                    if (warriors.get(n).getId() == allyId) {
                                        found = true;
                                        WarriorHelper.putWarriorIntoMap(warriors.get(n), battleMap, x, y);
                                        break;
                                    }
                                }
                                if (found) {
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("This map element is not recognized (type identifier = " + typeId +")");
            }
        }

        return battleMap;
    }


    public static BattleMap deserializeLightBattleMap(MicroByteBuffer buffer, BaseConfiguration baseConfiguration){
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
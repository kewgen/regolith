package com.geargames.regolith.serializers;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.BattleScreen;
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

        ClientHumanElementCollection groupUnits = ClientBattleHelper.getBattleUnits(battle, account);
        ClientHumanElementCollection allyUnits = ClientBattleHelper.getAllyBattleUnits(battle, account);
        ClientHumanElementCollection enemyUnits = ClientBattleHelper.getEnemyBattleUnits(battle, account);

        BattleScreen battleScreen = PRegolithPanelManager.getInstance().getBattleScreen();
        battleScreen.setGroupUnits(groupUnits);
        battleScreen.setAllyUnits(allyUnits);
        battleScreen.setEnemyUnits(enemyUnits);

        while (buffer.getPosition() != last) {
            short cellX = SimpleDeserializer.deserializeShort(buffer);
            short cellY = SimpleDeserializer.deserializeShort(buffer);
            short typeId = SimpleDeserializer.deserializeShort(buffer);
            if (typeId == SerializeHelper.findTypeId("Warrior")) {
                int warriorId = SimpleDeserializer.deserializeInt(buffer);
                HumanElement unit = BattleMapHelper.getHumanElementByHumanId(groupUnits, warriorId);
                WarriorHelper.putWarriorIntoMap(cells, unit, cellX, cellY);
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
                                    Warrior warrior = warriors.get(n);
                                    if (warrior.getId() == allyId) {
                                        found = true;
                                        HumanElement unit = BattleMapHelper.getHumanElementByHuman(groupUnits, warrior);
                                        WarriorHelper.putWarriorIntoMap(cells, unit, cellX, cellY);
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
            } else if (typeId == SerializeHelper.findTypeId("Box")) {
                ClientBox box = new ClientBox();
                cells[cellX][cellY].addElement(box);
                deserializeBox(box, buffer, baseConfiguration);
            } else if (typeId == SerializeHelper.findTypeId("Magazine")) {
                Magazine magazine = new Magazine();
                cells[cellX][cellY].addElement(magazine);
                deserializeMagazine(magazine, buffer, baseConfiguration);
            } else if (typeId == SerializeHelper.findTypeId("Barrier")) {
                cells[cellX][cellY].addElement(BaseConfigurationHelper.findBarrierById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
            } else if (typeId == SerializeHelper.findTypeId("Armor")) {
                Armor armor = new Armor();
                cells[cellX][cellY].addElement(armor);
                TackleDeserializer.deSerialize(armor, buffer, baseConfiguration);
            } else if (typeId == SerializeHelper.findTypeId("Weapon")) {
                Weapon weapon = new Weapon();
                cells[cellX][cellY].addElement(weapon);
                TackleDeserializer.deSerialize(weapon, buffer, baseConfiguration);
            } else if (typeId == SerializeHelper.findTypeId("Medikit")) {
                Medikit medikit = new Medikit();
                cells[cellX][cellY].addElement(medikit);
                TackleDeserializer.deserializeMedikit(medikit, buffer, baseConfiguration);
            } else {
                throw new IllegalArgumentException("This map element is not recognized (type identifier = " + typeId + ")");
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
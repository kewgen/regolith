package com.geargames.regolith.serializers;

import com.geargames.regolith.*;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientBattleGroupCollection;
import com.geargames.regolith.units.map.BattleMap;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 13.06.12
 */
public class ClientMessageDeserializer {

    public static Object deSerializeLoginConfirmation(MicroByteBuffer buffer) {
        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        if (SimpleDeserializer.deserializeBoolean(buffer)) {
            clientConfiguration.setBaseConfiguration(ConfigurationDeserializer.deserializeBaseConfiguration(buffer));
            return AccountDeserializer.deserialize(buffer, clientConfiguration.getBaseConfiguration());
        } else {
            return SimpleDeserializer.deserializeString(buffer);
        }
    }

    public static Battle deSerializeListenToBattle(Battle battle, MicroByteBuffer buffer, BaseConfiguration baseConfiguration) {
        if (SimpleDeserializer.deserializeBoolean(buffer)) {
            battle.setId(SimpleDeserializer.deserializeInt(buffer));
            battle.setName(SimpleDeserializer.deserializeString(buffer));
            BattleType type = BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration);
            battle.setBattleType(type);
            byte alliancesLength = buffer.get();
            battle.setAlliances(new BattleAlliance[alliancesLength]);
            for (int i = 0; i < alliancesLength; i++) {
                BattleAlliance alliance = new BattleAlliance();
                alliance.setId(SimpleDeserializer.deserializeInt(buffer));
                alliance.setAllies(new ClientBattleGroupCollection(new Vector()));
                for (int j = 0; j < battle.getBattleType().getAllianceSize(); j++) {
                    BattleGroup group = new BattleGroup();
                    group.setId(SimpleDeserializer.deserializeInt(buffer));
                    int id = SimpleDeserializer.deserializeInt(buffer);
                    if (id != -1) {
                        Account account = new Account();
                        account.setId(id);
                        group.setAccount(account);
                        account.setName(SimpleDeserializer.deserializeString(buffer));
                    }
                    group.setAlliance(alliance);
                    alliance.getAllies().add(group);
                }
                alliance.setBattle(battle);
                battle.getAlliances()[i] = alliance;
            }
            return battle;
        } else {
            return null;
        }
    }

    public static BattleGroup deSerializeJoinToBattleAlliance(BattleAlliance alliance, MicroByteBuffer buffer) {
        if (SimpleDeserializer.deserializeBoolean(buffer)) {
            int id = SimpleDeserializer.deserializeInt(buffer);
            BattleGroupCollection groups = alliance.getAllies();
            for (int i = 0; i < groups.size(); i++) {
                if (groups.get(i).getId() == id) {
                    return groups.get(i);
                }
            }
            throw new IllegalStateException();
        } else {
            return null;
        }
    }

    public static Battle[] deSerializeBrowseCreatedBattles(MicroByteBuffer buffer, BaseConfiguration baseConfiguration){
        Battle[] battles = new Battle[SimpleDeserializer.deserializeInt(buffer)];

        for(int i = 0; i < battles.length; i++){
            Battle battle = new Battle();

            battle.setId(SimpleDeserializer.deserializeInt(buffer));
            battle.setName(SimpleDeserializer.deserializeString(buffer));
            battle.setBattleType(BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
            BattleMap map = new BattleMap();
            int length = buffer.get();
            BattleType[] types = new BattleType[length];
            for(int j= 0 ; j < length; j++){
                types[j] = BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration);
            }
            map.setPossibleBattleTypes(types);
            map.setName(SimpleDeserializer.deserializeString(buffer));
            battle.setMap(map);

            battles[i] = battle;
        }

        return battles;
    }

    public static BattleMap[] deSerializeBrowseBattleMaps(MicroByteBuffer buffer, BaseConfiguration baseConfiguration){
        int length = SimpleDeserializer.deserializeShort(buffer);
        BattleMap[] battleMaps = new BattleMap[length];
        for(int i = 0; i < length; i++){
            BattleMap battleMap = new BattleMap();
            battleMap.setId(SimpleDeserializer.deserializeInt(buffer));
            int len = buffer.get();
            BattleType[] types = new BattleType[len];
            for(int j = 0; j < len; j++){
                types[j] = BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration);
            }
            battleMap.setName(SimpleDeserializer.deserializeString(buffer));
            battleMaps[i] = battleMap;
        }
        return battleMaps;
    }

}

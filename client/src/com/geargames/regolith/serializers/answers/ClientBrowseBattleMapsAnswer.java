package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: mkutuzov, abarakov
 * Date: 06.07.12
 */
public class ClientBrowseBattleMapsAnswer extends ClientDeSerializedMessage {
    private ClientConfiguration configuration;
    private BattleMap[] battleMaps;

    public ClientBrowseBattleMapsAnswer(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public BattleMap[] getBattleMaps() {
        return battleMaps;
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        int size = SimpleDeserializer.deserializeShort(buffer);
        BattleMap[] battleMaps = new BattleMap[size];
        for (int i = 0; i < size; i++) {
            BattleMap battleMap = new BattleMap();
            battleMap.setId(SimpleDeserializer.deserializeInt(buffer));
            int amount = buffer.get();
            battleMap.setPossibleBattleTypes(new BattleType[amount]);
            for (int j = 0; j < amount; j++) {
                battleMap.getPossibleBattleTypes()[j] = BaseConfigurationHelper.findBattleTypeById(
                        SimpleDeserializer.deserializeInt(buffer), configuration.getBaseConfiguration());
            }
            battleMap.setName(SimpleDeserializer.deserializeString(buffer));
            battleMaps[i] = battleMap;
        }
        this.battleMaps = battleMaps;
    }

}

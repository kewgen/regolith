package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mkutuzov
 * Date: 06.07.12
 */
public class ClientBrowseBattleMapsAnswer extends ClientDeSerializedMessage {
    private BattleMap[] battleMaps;

    private BaseConfiguration baseConfiguration;

    public ClientBrowseBattleMapsAnswer(BaseConfiguration baseConfiguration) {
        this.baseConfiguration = baseConfiguration;
    }

    public BattleMap[] getBattleMaps() {
        return battleMaps;
    }

    public void deSerialize(MicroByteBuffer buffer) {
        int length = SimpleDeserializer.deserializeShort(buffer);
        BattleMap[] battleMaps = new BattleMap[length];
        for (int i = 0; i < length; i++) {
            BattleMap battleMap = new BattleMap();
            battleMap.setId(SimpleDeserializer.deserializeInt(buffer));
            int amount = buffer.get();
            battleMap.setPossibleBattleTypes(new BattleType[amount]);
            for (int j = 0; j < amount; j++) {
                battleMap.getPossibleBattleTypes()[j] = BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration);
            }
            battleMap.setName(SimpleDeserializer.deserializeString(buffer));
            battleMaps[i] = battleMap;
        }
        this.battleMaps = battleMaps;
    }
}

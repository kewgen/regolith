package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.serializers.BattleMapDeserializer;
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
            battleMaps[i] = BattleMapDeserializer.deserializeLightBattleMap(buffer, configuration.getBaseConfiguration());
        }
        this.battleMaps = battleMaps;
    }

}

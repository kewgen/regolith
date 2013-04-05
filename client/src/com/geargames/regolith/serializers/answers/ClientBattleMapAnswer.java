package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.serializers.BattleMapDeserializer;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: m.v.kutuzov
 * Date: 27.03.13
 */
public class ClientBattleMapAnswer extends ClientDeSerializedMessage {
    private BattleMap battleMap;

    public BattleMap getBattleMap() {
        return battleMap;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        boolean success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            battleMap = BattleMapDeserializer.deserializeLightBattleMap(buffer, ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
        } else {
            battleMap = null;
        }
    }

    public boolean isSuccess() {
        return battleMap != null;
    }

}

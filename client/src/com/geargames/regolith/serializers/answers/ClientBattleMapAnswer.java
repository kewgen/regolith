package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.ErrorCodes;
import com.geargames.regolith.serializers.BattleMapDeserializer;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: m.v.kutuzov, abarakov
 * Date: 27.03.13
 */
public class ClientBattleMapAnswer extends ClientDeSerializedMessage {
    private BattleMap battleMap;
    private short errorCode;

    public BattleMap getBattleMap() {
        return battleMap;
    }

    public short getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return errorCode == ErrorCodes.SUCCESS;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        battleMap = null;
        errorCode = SimpleDeserializer.deserializeShort(buffer);
        if (errorCode == ErrorCodes.SUCCESS) {
            battleMap = BattleMapDeserializer.deserializeLightBattleMap(buffer, ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
        }
    }

}

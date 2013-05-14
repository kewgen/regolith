package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ErrorCodes;
import com.geargames.regolith.serializers.BattleMapDeserializer;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: mkutuzov, abarakov
 * Date: 06.07.12
 */
public class ClientBattleMapListAnswer extends ClientDeSerializedMessage {
    private ClientConfiguration configuration;
    private BattleMap[] battleMaps;
    private short errorCode;

    public ClientBattleMapListAnswer(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public BattleMap[] getBattleMaps() {
        return battleMaps;
    }

    public short getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return errorCode == ErrorCodes.SUCCESS;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        battleMaps = null;
        errorCode = SimpleDeserializer.deserializeShort(buffer);
        if (errorCode == ErrorCodes.SUCCESS) {
            short length = SimpleDeserializer.deserializeShort(buffer);
            BattleMap[] battleMaps = new BattleMap[length];
            for (int i = 0; i < length; i++) {
                battleMaps[i] = BattleMapDeserializer.deserializeLightBattleMap(buffer, configuration.getBaseConfiguration());
            }
            this.battleMaps = battleMaps;
        }
    }

}

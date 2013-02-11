package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleSerializer;

/**
 *  Запрос на начало битвы.
 */
public class StartBattleRequest extends ClientSerializedMessage {

    public StartBattleRequest(ClientConfiguration configuration) {
        super(configuration);
    }

    public short getType() {
        return Packets.START_BATTLE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(getConfiguration().getAccount(), buffer);
    }
}

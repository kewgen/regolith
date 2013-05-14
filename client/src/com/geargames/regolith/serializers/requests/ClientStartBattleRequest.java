package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;

/**
 *  Запрос на начало битвы.
 */
public class ClientStartBattleRequest extends ClientSerializedMessage {

    public ClientStartBattleRequest(ClientConfiguration configuration) {
        super(configuration);
    }

    public short getType() {
        return Packets.START_BATTLE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(getConfiguration().getAccount(), buffer);
    }
}

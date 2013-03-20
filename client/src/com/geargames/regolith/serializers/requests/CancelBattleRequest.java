package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;

/**
 * @author Mikhail_Kutuzov
 *         created: 26.06.12  10:17
 */
public class CancelBattleRequest extends ClientSerializedMessage {

    public CancelBattleRequest(ClientConfiguration configuration) {
        super(configuration);
    }

    public short getType() {
        return Packets.CANCEL_BATTLE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(getConfiguration().getAccount(), buffer);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;

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
        SimpleSerializer.serializeEntityReference(getConfiguration().getAccount(), buffer);
    }
}

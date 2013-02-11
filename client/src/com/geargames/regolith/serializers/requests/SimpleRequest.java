package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Date: 22.06.12
 */
public class SimpleRequest extends ClientSerializedMessage {
    private short type;

    public SimpleRequest(ClientConfiguration configuration, short type) {
        super(configuration);
        this.type = type;
    }

    public short getType() {
        return type;
    }

    public void serialize(MicroByteBuffer buffer) {
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;

/**
 *
 */
public class ClientChangeBaseLocationRequest extends ClientSerializedMessage{
    private Short type;

    public ClientChangeBaseLocationRequest(ClientConfiguration configuration, Short type) {
        super(configuration);
        this.type = type;
    }

    public short getType() {
        return type;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(type, buffer);
    }
}

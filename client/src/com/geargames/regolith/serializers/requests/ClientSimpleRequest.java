package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.common.serialization.MicroByteBuffer;

/**
 * User: mkutuzov
 * Date: 22.06.12
 */
public class ClientSimpleRequest extends ClientSerializedMessage {
    private short type;

    public ClientSimpleRequest(ClientConfiguration configuration, short type) {
        super(configuration);
        this.type = type;
    }

    public short getType() {
        return type;
    }

    public void serialize(MicroByteBuffer buffer) {
    }

}

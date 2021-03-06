package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;

/**
 * Users: mkutuzov, abarakov
 * Date: 20.06.12
 */
public class ClientLogoutRequest extends ClientSerializedMessage {

    public ClientLogoutRequest(ClientConfiguration configuration) {
        super(configuration);
    }

    public short getType() {
        return Packets.LOGOUT;
    }

    public void serialize(MicroByteBuffer buffer) {

    }

}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;

/**
 * User: mkutuzov
 * Date: 20.06.12
 */
public class LogoutRequest extends ClientSerializedMessage {
    private String name;

    public LogoutRequest(ClientConfiguration configuration, String name) {
        super(configuration);
        this.name = name;
    }

    public short getType() {
        return Packets.LOGOUT;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(name, buffer);
    }
}
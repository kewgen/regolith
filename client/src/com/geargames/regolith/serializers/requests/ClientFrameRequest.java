package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;

/**
 * User: mikhail v. kutuzov
 * Запрос на получение картики.
 */
public class ClientFrameRequest extends ClientSerializedMessage {
    private int frameId;

    public ClientFrameRequest(ClientConfiguration configuration) {
        super(configuration);
    }

    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    public short getType() {
        return Packets.FRAME_MESSAGE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(frameId, buffer);
    }
}

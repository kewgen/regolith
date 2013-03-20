package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;

/**
 * User: mikhail v. kutuzov
 * Date: 24.08.12
 * Time: 14:48
 */
public class ServerMessageIsLateAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;

    public ServerMessageIsLateAnswer(MicroByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public short getType() {
        return Packets.MESSAGE_IS_LATE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
    }
}

package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;

/**
 * User: mvkutuzov
 * Date: 06.05.13
 * Time: 12:36
 */
public class ServerCloseBattleMessage extends SerializedMessage {
    private MicroByteBuffer buffer;
    private String reason;

    public ServerCloseBattleMessage(MicroByteBuffer buffer, String reason) {
        this.buffer = buffer;
        this.reason = reason;
    }

    @Override
    public short getType() {
        return Packets.SERVER_CLOSE_BATTLE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(reason, buffer);
    }
}

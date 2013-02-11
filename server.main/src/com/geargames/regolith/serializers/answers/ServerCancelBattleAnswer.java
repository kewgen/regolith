package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerCancelBattleAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;

    public ServerCancelBattleAnswer(MicroByteBuffer buffer) {
        this.buffer = buffer;
    }

    public short getType() {
        return Packets.CANCEL_BATTLE;
    }

    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    public void serialize(MicroByteBuffer buffer) {
    }
}

package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Date: 05.08.12
 * Time: 15:32
 */
public class ServerStopListenAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Battle battle;

    @Override
    public short getType() {
        return Packets.DO_NOT_LISTEN_TO_CREATED_BATTLE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    public ServerStopListenAnswer(MicroByteBuffer buffer, Battle battle){
        this.buffer = buffer;
        this.battle = battle;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battle, buffer);
    }
}

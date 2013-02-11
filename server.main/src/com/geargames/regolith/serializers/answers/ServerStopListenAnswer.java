package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.Battle;

/**
 * Created with IntelliJ IDEA.
 * User: olga
 * Date: 05.08.12
 * Time: 15:32
 * To change this template use File | Settings | File Templates.
 */
public class ServerStopListenAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Battle battle;

    @Override
    public short getType() {
        return Packets.DO_NOT_LISTEN_TO_BATTLE;
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
        SimpleSerializer.serializeEntityReference(battle, buffer);
    }
}

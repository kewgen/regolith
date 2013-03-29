package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: m.v.kutuzov
 * Date: 28.03.13
 */
public class ServerListenToDeletedBattleAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Battle battle;

    public ServerListenToDeletedBattleAnswer(MicroByteBuffer buffer, Battle battle) {
        this.buffer = buffer;
        this.battle = battle;
    }

    @Override
    public short getType() {
        return Packets.LISTEN_TO_DELETED_BATTLE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battle, buffer);
    }
}

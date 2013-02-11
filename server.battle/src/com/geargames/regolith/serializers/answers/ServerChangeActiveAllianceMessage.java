package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mkutuzov
 * Date: 20.07.12
 */
public class ServerChangeActiveAllianceMessage extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleAlliance alliance;

    public ServerChangeActiveAllianceMessage(MicroByteBuffer buffer, BattleAlliance alliance) {
        this.buffer = buffer;
        this.alliance = alliance;
    }

    @Override
    public short getType() {
        return Packets.CHANGE_ACTIVE_ALLIANCE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(alliance, buffer);
    }
}

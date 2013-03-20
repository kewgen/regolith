package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerBrowseCreatedBattlesAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Battle[] battles;

    public ServerBrowseCreatedBattlesAnswer(MicroByteBuffer buffer, Battle[] battles) {
        this.buffer = buffer;
        this.battles = battles;
    }

    @Override
    public short getType() {
        return Packets.BROWSE_CREATED_BATTLES;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(battles.length, buffer);
        for(Battle battle : battles){
            SerializeHelper.serializeEntityReference(battle, buffer);
            SimpleSerializer.serialize(battle.getName(), buffer);
            SerializeHelper.serializeEntityReference(battle.getBattleType(), buffer);
            SimpleSerializer.serialize(battle.getMap().getName(), buffer);
        }
    }
}

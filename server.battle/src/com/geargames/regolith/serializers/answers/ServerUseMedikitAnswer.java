package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Date: 01.10.12
 * Time: 13:56
 */
public class ServerUseMedikitAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;

    public ServerUseMedikitAnswer(MicroByteBuffer buffer, Warrior warrior) {
        this.buffer = buffer;
        this.warrior = warrior;
    }

    @Override
    public short getType() {
        return Packets.USE_MEDIKIT;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(warrior, buffer);
        SimpleSerializer.serialize(warrior.getHealth(), buffer);
    }
}

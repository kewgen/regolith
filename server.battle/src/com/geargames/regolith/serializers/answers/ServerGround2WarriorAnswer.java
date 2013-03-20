package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Warrior;

/**
 * Created with IntelliJ IDEA.
 * User: olga
 * Date: 25.07.12
 * Time: 12:27
 * To change this template use File | Settings | File Templates.
 */
public class ServerGround2WarriorAnswer extends SerializedMessage {
    private short type;
    private short x;
    private short y;
    private Warrior warrior;
    private MicroByteBuffer buffer;

    public ServerGround2WarriorAnswer(MicroByteBuffer buffer, short type, short x, short y, Warrior warrior) {
        this.type = type;
        this.buffer = buffer;
        this.warrior = warrior;
        this.x = x;
        this.y = y;
    }

    public short getType() {
        return type;
    }

    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(warrior, buffer);
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}

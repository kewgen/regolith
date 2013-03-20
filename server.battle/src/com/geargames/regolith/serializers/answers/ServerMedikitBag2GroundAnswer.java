package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mikhail v. kutuzov
 * Date: 31.08.12
 * Time: 15:27
 */
public class ServerMedikitBag2GroundAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private short number;
    private short x;
    private short y;
    private boolean success;

    public static ServerMedikitBag2GroundAnswer answerFailure(MicroByteBuffer buffer){
        return new ServerMedikitBag2GroundAnswer(buffer, null, (short)0, (short)0, (short)0, true);
    }

    public static ServerMedikitBag2GroundAnswer answerSuccess(MicroByteBuffer buffer, Warrior warrior, short number, short x, short y) {
        return new ServerMedikitBag2GroundAnswer(buffer, warrior, number, x, y, true);
    }

    private ServerMedikitBag2GroundAnswer(MicroByteBuffer buffer, Warrior warrior, short number, short x, short y, boolean success) {
        this.buffer = buffer;
        this.warrior = warrior;
        this.number = number;
        this.x = x;
        this.y = y;
        this.success = success;
    }

    @Override
    public short getType() {
        return Packets.TAKE_MEDIKIT_FROM_BAG_PUT_INTO_GROUND;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SerializeHelper.serializeEntityReference(warrior, buffer);
            SimpleSerializer.serialize(number, buffer);
            SimpleSerializer.serialize(x, buffer);
            SimpleSerializer.serialize(y, buffer);
        }
    }
}

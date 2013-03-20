package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.battle.Warrior;

/**
 * Created with IntelliJ IDEA.
 * User: olga
 * Date: 09.07.12
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */
public class ServerJoinBaseWarriorsAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior[] warriors;
    private boolean success;

    public static ServerJoinBaseWarriorsAnswer failureAnswer(MicroByteBuffer buffer) {
        return new ServerJoinBaseWarriorsAnswer(buffer, null, false);
    }

    public static ServerJoinBaseWarriorsAnswer successAnswer(MicroByteBuffer buffer, Warrior[] warriors) {
        return new ServerJoinBaseWarriorsAnswer(buffer, warriors, true);
    }

    private ServerJoinBaseWarriorsAnswer(MicroByteBuffer buffer, Warrior[] warriors, boolean success) {
        this.success = success;
        this.buffer = buffer;
        this.warriors = warriors;
    }

    @Override
    public short getType() {
        return Packets.JOIN_BASE_WARRIORS_TO_ACCOUNT;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SimpleSerializer.serialize(warriors.length, buffer);
            for (int i = 0; i < warriors.length; i++) {
                AccountSerializer.serialize(warriors[i], buffer);
            }
        }
    }
}

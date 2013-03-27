package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: abarakov
 * Date: 21.03.13
 */
public class ServerGroupReadyStateAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleGroup battleGroup;
    private short type;
    private boolean success;

    public static ServerGroupReadyStateAnswer answerSuccess(MicroByteBuffer buffer, short type, BattleGroup battleGroup) {
        return new ServerGroupReadyStateAnswer(buffer, battleGroup, type, true);
    }

    public static ServerGroupReadyStateAnswer answerFailure(MicroByteBuffer buffer, short type) {
        return new ServerGroupReadyStateAnswer(buffer, null, type, false);
    }

    private ServerGroupReadyStateAnswer(MicroByteBuffer buffer, BattleGroup battleGroup, short type, boolean success) {
        this.buffer = buffer;
        this.battleGroup = battleGroup;
        this.type = type;
        this.success = success;
    }

    public short getType() {
        return type;
    }

    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SerializeHelper.serializeEntityReference(battleGroup, buffer);
        }
    }

}

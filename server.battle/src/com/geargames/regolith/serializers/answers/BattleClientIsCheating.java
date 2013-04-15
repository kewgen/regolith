package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.Account;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 15.04.13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class BattleClientIsCheating extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Account cheater;

    public BattleClientIsCheating(MicroByteBuffer buffer, Account cheater) {
        this.buffer = buffer;
        this.cheater = cheater;
    }

    @Override
    public short getType() {
        return Packets.CONTROL_SUM;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(cheater, buffer);
    }
}

package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerJoinToBattleAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleGroup battleGroup;
    private Account account;
    private boolean success;

    public static final ServerJoinToBattleAnswer AnswerSuccess(MicroByteBuffer buffer, BattleGroup battleGroup, Account account) {
        return new ServerJoinToBattleAnswer(buffer, battleGroup, account, true);
    }

    public static final ServerJoinToBattleAnswer AnswerFailure(MicroByteBuffer buffer) {
        return new ServerJoinToBattleAnswer(buffer, null, null, true);
    }

    private ServerJoinToBattleAnswer(MicroByteBuffer buffer, BattleGroup battleGroup, Account account, boolean success) {
        this.buffer = buffer;
        this.battleGroup = battleGroup;
        this.account = account;
        this.success = success;
    }

    public short getType() {
        return Packets.JOIN_TO_BATTLE_ALLIANCE;
    }

    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SimpleSerializer.serializeEntityReference(battleGroup, buffer);
        }
    }

}

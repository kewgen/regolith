package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.AccountSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerJoinToBattleAllianceAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleGroup battleGroup;
    private Account account;
    private boolean success;

    public static ServerJoinToBattleAllianceAnswer AnswerSelfSuccess(MicroByteBuffer buffer, BattleGroup battleGroup) {
        return new ServerJoinToBattleAllianceAnswer(buffer, battleGroup, null, true);
    }

    public static ServerJoinToBattleAllianceAnswer AnswerSuccess(MicroByteBuffer buffer, BattleGroup battleGroup, Account account) {
        return new ServerJoinToBattleAllianceAnswer(buffer, battleGroup, account, true);
    }

    public static ServerJoinToBattleAllianceAnswer AnswerFailure(MicroByteBuffer buffer) {
        return new ServerJoinToBattleAllianceAnswer(buffer, null, null, false);
    }

    private ServerJoinToBattleAllianceAnswer(MicroByteBuffer buffer, BattleGroup battleGroup, Account account, boolean success) {
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
            SerializeHelper.serializeEntityReference(battleGroup, buffer);
            if (account != null) {
                SimpleSerializer.serialize(true, buffer);
                AccountSerializer.serialize(account, buffer);
            } else {
                SimpleSerializer.serialize(false, buffer);
            }
        }
    }

}

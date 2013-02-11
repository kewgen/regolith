package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerEvictAccountFromAllianceAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Account account;
    private BattleAlliance alliance;
    private boolean success;

    public static final ServerEvictAccountFromAllianceAnswer AnswerSuccess(MicroByteBuffer buffer, Account account, BattleAlliance alliance){
        return new ServerEvictAccountFromAllianceAnswer(buffer, account, alliance, true);
    }

    public static final ServerEvictAccountFromAllianceAnswer AnswerFailure(MicroByteBuffer buffer){
        return new ServerEvictAccountFromAllianceAnswer(buffer, null, null, false);
    }

    private ServerEvictAccountFromAllianceAnswer(MicroByteBuffer buffer, Account account, BattleAlliance alliance, boolean success) {
        this.buffer = buffer;
        this.account = account;
        this.alliance = alliance;
        this.success = success;
    }

    @Override
    public short getType() {
        return Packets.EVICT_ACCOUNT_FROM_ALLIANCE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if(success){
            SimpleSerializer.serializeEntityReference(alliance, buffer);
            SimpleSerializer.serializeEntityReference(account, buffer);
        }
    }
}


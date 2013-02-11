package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.BattleSerializer;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.service.remote.BattleServiceDescriptor;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class StartBattleAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private boolean successfully;
    private Battle battle;
    private Account account;
    private BattleServiceDescriptor battleServiceDescriptor;

    public static StartBattleAnswer answerSuccess(MicroByteBuffer buffer, Battle battle, Account account, BattleServiceDescriptor battleServiceDescriptor){
        return new StartBattleAnswer(buffer,battle,account, battleServiceDescriptor,true);
    }

    public static StartBattleAnswer answerFailure(MicroByteBuffer buffer){
        return new StartBattleAnswer(buffer,null,null,null,false);
    }

    private StartBattleAnswer(MicroByteBuffer buffer, Battle battle, Account account, BattleServiceDescriptor battleServiceDescriptor, boolean successfully) {
        this.buffer = buffer;
        this.successfully = successfully;
        this.battle = battle;
        this.account = account;
        this.battleServiceDescriptor = battleServiceDescriptor;
    }

    public short getType() {
        return Packets.START_BATTLE;
    }

    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(successfully, buffer);
        if (successfully) {
            BattleSerializer.serialize(battle, account, buffer);
            SimpleSerializer.serialize(battleServiceDescriptor.getHost(), buffer);
            SimpleSerializer.serialize(battleServiceDescriptor.getPort(), buffer);
        }
    }
}

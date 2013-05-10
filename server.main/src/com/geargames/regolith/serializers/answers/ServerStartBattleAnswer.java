package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.BattleSerializer;
import com.geargames.regolith.service.remote.BattleServiceDescriptor;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerStartBattleAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private boolean successfully;
    private Battle battle;
    private Account account;
    private BattleServiceDescriptor battleServiceDescriptor;

    public static ServerStartBattleAnswer answerSuccess(MicroByteBuffer buffer, Battle battle, Account account, BattleServiceDescriptor battleServiceDescriptor) {
        return new ServerStartBattleAnswer(buffer, battle, account, battleServiceDescriptor, true);
    }

    public static ServerStartBattleAnswer answerFailure(MicroByteBuffer buffer) {
        return new ServerStartBattleAnswer(buffer, null, null, null, false);
    }

    private ServerStartBattleAnswer(MicroByteBuffer buffer, Battle battle, Account account, BattleServiceDescriptor battleServiceDescriptor, boolean successfully) {
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
            BattleSerializer.serializeBattle(battle, account, buffer);
            SimpleSerializer.serialize(battleServiceDescriptor.getHost(), buffer);
            SimpleSerializer.serialize(battleServiceDescriptor.getPort(), buffer);
        }
    }
}

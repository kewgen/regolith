package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mikhail v. kutuzov
 * Date: 15.08.12
 * Time: 20:39
 */
public class BattleServiceNewClientLogin extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Battle battle;
    private BattleGroup battleGroup;

    public BattleServiceNewClientLogin(MicroByteBuffer buffer, Battle battle, BattleGroup battleGroup) {
        this.buffer = buffer;
        this.battle = battle;
        this.battleGroup = battleGroup;
    }

    @Override
    public short getType() {
        return Packets.BATTLE_SERVICE_NEW_CLIENT_LOGIN;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(battle, buffer);
        SimpleSerializer.serializeEntityReference(battleGroup, buffer);
    }
}
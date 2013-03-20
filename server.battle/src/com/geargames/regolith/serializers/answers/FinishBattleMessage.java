package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mikhail v. kutuzov
 * Date: 09.10.12
 * Time: 11:18
 * Конец битвы.
 * Собщение содержит изменённые, под конец битвы, очки опыта бойцов отряда и победивший альянс.
 */
public class FinishBattleMessage extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleAlliance winner;

    public FinishBattleMessage(MicroByteBuffer buffer, BattleAlliance winner) {
        this.buffer = buffer;
        this.winner = winner;
    }

    @Override
    public short getType() {
        return Packets.FINISH_BATTLE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(winner, buffer);
        SimpleSerializer.serialize(winner.getBattle().getBattleType().getScores(), buffer);
    }
}

package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mkutuzov
 * Date: 22.06.12
 */
public class ServerBrowseBattleMapsAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleMap[] maps;

    public ServerBrowseBattleMapsAnswer(MicroByteBuffer buffer, BattleMap[] maps) {
        this.buffer = buffer;
        this.maps = maps;
    }

    @Override
    public short getType() {
        return Packets.BROWSE_BATTLE_MAPS;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize((short) maps.length, buffer);
        for (BattleMap map : maps) {
            SimpleSerializer.serializeEntityReference(map, buffer);
            BattleType[] possibilities = map.getPossibleBattleTypes();
            SimpleSerializer.serialize((byte) possibilities.length, buffer);
            int length = possibilities.length;
            for (int i = 0; i < length; i++) {
                SimpleSerializer.serializeEntityReference(possibilities[i], buffer);
            }
            SimpleSerializer.serialize(map.getName(), buffer);
        }
    }
}

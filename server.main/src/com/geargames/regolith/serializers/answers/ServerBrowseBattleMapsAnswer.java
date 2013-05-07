package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.ErrorCodes;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: mkutuzov, abarakov
 * Date: 22.06.12
 */
public class ServerBrowseBattleMapsAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleMap[] maps;
    private short errorCode;

    public static ServerBrowseBattleMapsAnswer answerSuccess(MicroByteBuffer buffer, BattleMap[] maps) {
        return new ServerBrowseBattleMapsAnswer(buffer, maps, ErrorCodes.SUCCESS);
    }

    public static ServerBrowseBattleMapsAnswer answerFailure(MicroByteBuffer buffer, short errorCode) {
        return new ServerBrowseBattleMapsAnswer(buffer, null, errorCode);
    }

    private ServerBrowseBattleMapsAnswer(MicroByteBuffer buffer, BattleMap[] maps, short errorCode) {
        this.buffer = buffer;
        this.maps = maps;
        this.errorCode = errorCode;
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
        SimpleSerializer.serialize(errorCode, buffer);
        if (errorCode == ErrorCodes.SUCCESS) {
            SimpleSerializer.serialize((short) maps.length, buffer);
            for (BattleMap map : maps) {
                SerializeHelper.serializeEntityReference(map, buffer);
                BattleType[] possibilities = map.getPossibleBattleTypes();
                byte length = (byte) possibilities.length;
                SimpleSerializer.serialize(length, buffer);
                for (int i = 0; i < length; i++) {
                    SerializeHelper.serializeEntityReference(possibilities[i], buffer);
                }
                SimpleSerializer.serialize(map.getName(), buffer);
            }
        }
    }

}

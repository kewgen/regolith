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
 * Users: m.v.kutuzov, abarakov
 * Date: 27.03.13
 */
public class ServerBrowseRandomBattleMapAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleMap map;
    private short errorCode;


    public static ServerBrowseRandomBattleMapAnswer answerSuccess(MicroByteBuffer buffer, BattleMap map) {
        return new ServerBrowseRandomBattleMapAnswer(buffer, map, ErrorCodes.SUCCESS);
    }


    public static ServerBrowseRandomBattleMapAnswer answerFailure(MicroByteBuffer buffer, short errorCode) {
        return new ServerBrowseRandomBattleMapAnswer(buffer, null, errorCode);
    }

    private ServerBrowseRandomBattleMapAnswer(MicroByteBuffer buffer, BattleMap map, short errorCode) {
        this.buffer = buffer;
        this.map = map;
        this.errorCode = errorCode;
    }

    @Override
    public short getType() {
        return Packets.BROWSE_RANDOM_BATTLE_MAP;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(errorCode, buffer);
        if (errorCode == ErrorCodes.SUCCESS) {
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

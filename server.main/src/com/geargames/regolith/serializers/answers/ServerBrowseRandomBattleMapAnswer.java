package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: m.v.kutuzov
 * Date: 27.03.13
 */
public class ServerBrowseRandomBattleMapAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private BattleMap map;
    private boolean success;


    public static ServerBrowseRandomBattleMapAnswer answerSuccess(MicroByteBuffer buffer, BattleMap map){
        return new ServerBrowseRandomBattleMapAnswer(buffer, map, true);
    }


    public static ServerBrowseRandomBattleMapAnswer answerFailure(MicroByteBuffer buffer){
        return new ServerBrowseRandomBattleMapAnswer(buffer, null, false);
    }

    private ServerBrowseRandomBattleMapAnswer(MicroByteBuffer buffer, BattleMap map, boolean success) {
        this.buffer = buffer;
        this.map = map;
        this.success = success;
    }

    @Override
    public short getType() {
        return Packets.BROWSE_RANDOM_BATTLE_MAP ;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if(success){
            SerializeHelper.serializeEntityReference(map, buffer);
            BattleType[] possibilities = map.getPossibleBattleTypes();
            SimpleSerializer.serialize((byte) possibilities.length, buffer);
            int length = possibilities.length;
            for (int i = 0; i < length; i++) {
                SerializeHelper.serializeEntityReference(possibilities[i], buffer);
            }
            SimpleSerializer.serialize(map.getName(), buffer);
        }
    }
}

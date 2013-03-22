package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.map.BattleMap;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
@Deprecated //todo: очень похож на ServerBrowseBattleMapsAnswer
public class ServerBrowseMapsAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private List<BattleMap> maps;

    public ServerBrowseMapsAnswer(MicroByteBuffer buffer, List<BattleMap> maps) {
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
        for(BattleMap map : maps){
            SerializeHelper.serializeEntityReference(map, buffer);
            SimpleSerializer.serialize(map.getName(), buffer);
            SimpleSerializer.serialize(map.getExits().length, buffer);
        }
    }
}

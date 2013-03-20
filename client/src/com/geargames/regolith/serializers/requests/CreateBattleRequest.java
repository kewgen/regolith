package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mkutuzov
 * Date: 20.06.12
 */
public class CreateBattleRequest extends ClientSerializedMessage {
    private BattleMap battleMap;
    private byte index;

    public CreateBattleRequest(ClientConfiguration configuration, BattleMap battleMap, byte index) {
        super(configuration);
        this.battleMap = battleMap;
        this.index = index;
    }

    public short getType() {
        return Packets.CREATE_BATTLE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battleMap, buffer);
        SimpleSerializer.serialize(index, buffer);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mkutuzov, abarakov
 * Date: 20.06.12
 */
public class CreateBattleRequest extends ClientSerializedMessage {
    private BattleMap battleMap;
    private BattleType battleType;

    public CreateBattleRequest(ClientConfiguration configuration, BattleMap battleMap, BattleType battleType) {
        super(configuration);
        this.battleMap = battleMap;
        this.battleType = battleType;
    }

    public short getType() {
        return Packets.CREATE_BATTLE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battleMap, buffer);
        SerializeHelper.serializeEntityReference(battleType, buffer);
    }

}

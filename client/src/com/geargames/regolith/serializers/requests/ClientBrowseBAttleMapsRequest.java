package com.geargames.regolith.serializers.requests;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleType;

/**
 * User: m.v.kutuzov
 * Date: 27.03.13
 */
public class ClientBrowseBattleMapsRequest extends ClientSerializedMessage {
    private BattleType battleType;

    public ClientBrowseBattleMapsRequest(ClientConfiguration configuration, BattleType battleType) {
        super(configuration);
        this.battleType = battleType;
    }

    @Override
    public short getType() {
        return Packets.BROWSE_BATTLE_MAPS;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battleType, buffer);
    }
}

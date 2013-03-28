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
public class ClientBrowseBattleMapsRequest1 extends ClientSerializedMessage {
    private BattleType battleType;

    public ClientBrowseBattleMapsRequest1(ClientConfiguration configuration) {
        super(configuration);
    }

    public void setBattleType(BattleType battleType) {
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

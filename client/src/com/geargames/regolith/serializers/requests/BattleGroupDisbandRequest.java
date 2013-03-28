package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mkutuzov, abarakov
 * Date: 27.06.12
 */
public class BattleGroupDisbandRequest extends ClientSerializedMessage {
    private BattleGroup group;

    public BattleGroupDisbandRequest(ClientConfiguration configuration, BattleGroup group) {
        super(configuration);
        this.group = group;
    }

    public short getType() {
        return Packets.GROUP_DISBAND;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(group.getAlliance().getBattle(), buffer);
        SerializeHelper.serializeEntityReference(group.getAlliance(), buffer);
        SerializeHelper.serializeEntityReference(group, buffer);
    }
}

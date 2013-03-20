package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mkutuzov
 * Date: 27.06.12
 */
public class GroupReadyStateRequest extends ClientSerializedMessage {
    private short type;
    private BattleGroup group;

    public GroupReadyStateRequest(ClientConfiguration configuration, short type, BattleGroup group) {
        super(configuration);
        this.type = type;
        this.group = group;
    }

    public short getType() {
        return type;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(group.getAlliance().getBattle(), buffer);
        SerializeHelper.serializeEntityReference(group.getAlliance(), buffer);
        SerializeHelper.serializeEntityReference(group, buffer);
    }
}

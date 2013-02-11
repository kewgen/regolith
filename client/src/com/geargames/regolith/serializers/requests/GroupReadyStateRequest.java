package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
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
        SimpleSerializer.serializeEntityReference(group.getAlliance().getBattle(), buffer);
        SimpleSerializer.serializeEntityReference(group.getAlliance(), buffer);
        SimpleSerializer.serializeEntityReference(group, buffer);
    }
}

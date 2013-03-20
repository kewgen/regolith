package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 22.06.12
 */
public class BattleGroupCompleteRequest extends ClientSerializedMessage {
    private BattleGroup group;
    private Warrior[] warriors;

    public BattleGroupCompleteRequest(ClientConfiguration configuration, Warrior[] warriors, BattleGroup group) {
        super(configuration);
        this.group = group;
        this.warriors = warriors;
    }

    public short getType() {
        return Packets.GROUP_COMPLETE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(group.getAlliance().getBattle(), buffer);
        SerializeHelper.serializeEntityReference(group.getAlliance(), buffer);
        SerializeHelper.serializeEntityReference(group, buffer);
        byte length = (byte) warriors.length;
        SimpleSerializer.serialize(length, buffer);
        for (int i = 0; i < length; i++) {
            SerializeHelper.serializeEntityReference(warriors[i], buffer);
        }
    }
}

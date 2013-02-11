package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

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
        SimpleSerializer.serializeEntityReference(group.getAlliance().getBattle(), buffer);
        SimpleSerializer.serializeEntityReference(group.getAlliance(), buffer);
        SimpleSerializer.serializeEntityReference(group, buffer);
        byte length = (byte) warriors.length;
        SimpleSerializer.serialize(length, buffer);
        for (int i = 0; i < length; i++) {
            SimpleSerializer.serializeEntityReference(warriors[i], buffer);
        }
    }
}

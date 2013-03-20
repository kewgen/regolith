package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

public class ClientMoveRequest extends ClientSerializedMessage {
    private Warrior warrior;
    private short x;
    private short y;

    public ClientMoveRequest(ClientConfiguration configuration, Warrior warrior, short x, short y) {
        super(configuration);
        this.warrior = warrior;
        this.x = x;
        this.y = y;
    }

    public short getType() {
        return Packets.MOVE_ALLY;
    }

    public void serialize(MicroByteBuffer buffer) {
        BattleGroup battleGroup = warrior.getBattleGroup();
        BattleAlliance alliance = battleGroup.getAlliance();
        SimpleSerializer.serialize(alliance.getNumber(), buffer);
        SerializeHelper.serializeEntityReference(battleGroup, buffer);
        SerializeHelper.serializeEntityReference(warrior, buffer);
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.map.HumanElement;

public class ClientMoveRequest extends ClientSerializedMessage {
    private HumanElement unit;
    private short x;
    private short y;

    public ClientMoveRequest(ClientConfiguration configuration, HumanElement unit, short x, short y) {
        super(configuration);
        this.unit = unit;
        this.x = x;
        this.y = y;
    }

    public short getType() {
        return Packets.MOVE_WARRIOR;
    }

    public void serialize(MicroByteBuffer buffer) {
        BattleGroup battleGroup = unit.getHuman().getBattleGroup();
        BattleAlliance alliance = battleGroup.getAlliance();
        SimpleSerializer.serialize(alliance.getNumber(), buffer);
        SerializeHelper.serializeEntityReference(battleGroup, buffer);
        SerializeHelper.serializeEntityReference(unit.getHuman(), buffer);
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }

}

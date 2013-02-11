package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

public class ClientServerGround2WarriorRequest extends ClientSerializedMessage {
    private short type;
    private Warrior warrior;
    private short x;
    private short y;

    public ClientServerGround2WarriorRequest(ClientConfiguration configuration, short type, Warrior warrior, short x, short y) {
        super(configuration);
        this.type = type;
        this.warrior = warrior;
        this.x = x;
        this.y = y;
    }

    public short getType() {
        return type;
    }

    public void serialize(MicroByteBuffer buffer) {
        BattleGroup group = warrior.getBattleGroup();
        BattleAlliance alliance = group.getAlliance();
        SimpleSerializer.serialize(alliance.getNumber(), buffer);
        SimpleSerializer.serializeEntityReference(group, buffer);
        SimpleSerializer.serializeEntityReference(warrior, buffer);
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}

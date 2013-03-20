package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;

public class ClientShootRequest extends ClientSerializedMessage {
    private Warrior hunter;
    private Warrior victim;

    public ClientShootRequest(ClientConfiguration configuration, Warrior hunter, Warrior victim) {
        super(configuration);
        this.hunter = hunter;
        this.victim = victim;
    }

    public short getType() {
        return Packets.SHOOT;
    }

    public void serialize(MicroByteBuffer buffer) {
        BattleGroup hunterGroup = hunter.getBattleGroup();
        BattleAlliance hunterAlliance = hunterGroup.getAlliance();

        SimpleSerializer.serialize(hunterAlliance.getNumber(), buffer);
        SerializeHelper.serializeEntityReference(hunterGroup, buffer);
        SerializeHelper.serializeEntityReference(hunter, buffer);

        BattleGroup victimGroup = victim.getBattleGroup();
        BattleAlliance victimAlliance = victimGroup.getAlliance();

        SimpleSerializer.serialize(victimAlliance.getNumber(), buffer);
        SerializeHelper.serializeEntityReference(victimGroup, buffer);
        SerializeHelper.serializeEntityReference(victim, buffer);
    }
}

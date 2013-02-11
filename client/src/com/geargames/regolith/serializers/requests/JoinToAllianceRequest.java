package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * Присоединиться к боевому союзу(альянсу).
 */
public class JoinToAllianceRequest extends ClientSerializedMessage {
    private BattleAlliance alliance;

    public JoinToAllianceRequest(ClientConfiguration configuration, BattleAlliance alliance) {
        super(configuration);
        this.alliance = alliance;
    }

    public short getType() {
        return Packets.JOIN_TO_BATTLE_ALLIANCE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(alliance.getBattle(), buffer);
        SimpleSerializer.serializeEntityReference(alliance, buffer);
    }
}

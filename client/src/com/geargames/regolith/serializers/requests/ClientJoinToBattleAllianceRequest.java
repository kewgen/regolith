package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * Сообщение-запрос о присоединении к военному союзу (альянсу).
 */
public class ClientJoinToBattleAllianceRequest extends ClientSerializedMessage {
    private BattleAlliance alliance;

    public ClientJoinToBattleAllianceRequest(ClientConfiguration configuration, BattleAlliance alliance) {
        super(configuration);
        this.alliance = alliance;
    }

    public short getType() {
        return Packets.JOIN_TO_BATTLE_ALLIANCE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(alliance.getBattle(), buffer);
        SerializeHelper.serializeEntityReference(alliance, buffer);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mkutuzov
 * Date: 20.06.12
 */
public class EvictAccountRequest extends ClientSerializedMessage {
    private Account account;
    private BattleAlliance alliance;

    public EvictAccountRequest(ClientConfiguration configuration, Account account, BattleAlliance alliance) {
        super(configuration);
        this.account = account;
        this.alliance = alliance;
    }

    public short getType() {
        return Packets.EVICT_ACCOUNT_FROM_ALLIANCE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(alliance.getBattle(), buffer);
        SimpleSerializer.serializeEntityReference(alliance,buffer);
        SimpleSerializer.serializeEntityReference(account,buffer);
    }
}

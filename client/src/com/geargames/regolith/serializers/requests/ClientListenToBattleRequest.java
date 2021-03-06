package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Послушать состояние формирующейся битвы.
 * Date: 21.06.12
 */
public class ClientListenToBattleRequest extends ClientSerializedMessage {
    private Battle battle;

    public ClientListenToBattleRequest(ClientConfiguration configuration, Battle battle) {
        super(configuration);
        this.battle = battle;
    }

    public short getType() {
        return Packets.LISTEN_TO_CREATED_BATTLE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battle, buffer);
    }
}

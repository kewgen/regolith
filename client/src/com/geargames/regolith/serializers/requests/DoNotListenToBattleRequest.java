package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Date: 26.06.12
 */
public class DoNotListenToBattleRequest extends ClientSerializedMessage {
    private Battle battle;

    public DoNotListenToBattleRequest(ClientConfiguration configuration, Battle battle) {
        super(configuration);
        this.battle = battle;
    }

    public short getType() {
        return Packets.DO_NOT_LISTEN_TO_BATTLE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(battle, buffer);
    }
}

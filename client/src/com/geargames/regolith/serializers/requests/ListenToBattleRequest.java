package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: mkutuzov
 * Послушать состояние формирующейся битвы.
 * Date: 21.06.12
 */
public class ListenToBattleRequest extends ClientSerializedMessage {
    private Battle battle;

    public ListenToBattleRequest(ClientConfiguration configuration, Battle battle) {
        super(configuration);
        this.battle = battle;
    }

    public short getType() {
        return Packets.LISTEN_TO_BATTLE;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(battle, buffer);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.Warrior;

/**
 * Присоеденить бойцов по-умолчанию к учётной записи пользователя.
 */
public class ClientJoinBaseWarriorsRequest extends ClientSerializedMessage {
    private Warrior[] warriors;

    public ClientJoinBaseWarriorsRequest(ClientConfiguration configuration, Warrior[] warriors) {
        super(configuration);
        this.warriors = warriors;
    }

    public short getType() {
        return Packets.JOIN_BASE_WARRIORS_TO_ACCOUNT;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(warriors.length, buffer);
        for (int i = 0; i < warriors.length; i++) {
            SimpleSerializer.serializeEntityReference(warriors[i], buffer);
            SimpleSerializer.serialize(warriors[i].getName(), buffer);
        }
    }
}

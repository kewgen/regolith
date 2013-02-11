package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Login;

/**
 * Запрос на соединения с игрой.
 */
public class LoginRequest extends ClientSerializedMessage {
    private Login login;

    public LoginRequest(ClientConfiguration configuration, Login login) {
        super(configuration);
        this.login = login;
    }

    public short getType() {
        return Packets.LOGIN;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(login.getName(), buffer);
        SimpleSerializer.serialize(login.getPassword(), buffer);
    }
}

package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Login;

/**
 * User: mkutuzov
 * Date: 20.06.12
 */
public class CreateLoginRequest extends ClientSerializedMessage {
    private Login login;

    public CreateLoginRequest(ClientConfiguration configuration, Login login) {
        super(configuration);
        this.login = login;
    }

    public short getType() {
        return Packets.CLIENT_REGISTRATION;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(login.getName(), buffer);
        SimpleSerializer.serialize(login.getPassword(), buffer);
    }

}

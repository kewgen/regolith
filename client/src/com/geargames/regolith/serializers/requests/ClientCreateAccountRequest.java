package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.units.Login;

/**
 * User: mkutuzov
 * Date: 20.06.12
 */
public class ClientCreateAccountRequest extends ClientSerializedMessage {
    private Login login;

    public ClientCreateAccountRequest(ClientConfiguration configuration, Login login) {
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

package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.Account;

/**
 * User: mkutuzov
 * Date: 29.06.12
 */
public class ServerLoginAnswer extends SerializedMessage {
    private Account account;
    private MicroByteBuffer buffer;
    private boolean success;
    private BaseConfiguration baseConfiguration;

    public static ServerLoginAnswer answerSuccess(MainServerConfiguration serverConfiguration, Account account, MicroByteBuffer buffer) {
        return new ServerLoginAnswer(serverConfiguration, account, buffer, true);
    }

    public static ServerLoginAnswer answerFailure(MicroByteBuffer buffer) {
        return new ServerLoginAnswer(null, null, buffer, false);
    }

    private ServerLoginAnswer(MainServerConfiguration serverConfiguration, Account account, MicroByteBuffer buffer, boolean success) {
        this.account = account;
        this.buffer = buffer;
        this.success = success;
        if(serverConfiguration != null){
            this.baseConfiguration = serverConfiguration.getRegolithConfiguration().getBaseConfiguration();
        }
    }

    @Override
    public short getType() {
        return Packets.LOGIN;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            ConfigurationSerializer.serialize(baseConfiguration, buffer);
            AccountSerializer.serialize(account, buffer);
        }
    }
}

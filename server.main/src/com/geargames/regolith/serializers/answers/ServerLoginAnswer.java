package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ErrorCodes;
import com.geargames.regolith.Packets;
import com.geargames.regolith.service.MainServerConfiguration;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.units.Account;

/**
 * Users: mkutuzov, abarakov
 * Date: 29.06.12
 */
public class ServerLoginAnswer extends SerializedMessage {
    private Account account;
    private MicroByteBuffer buffer;
    private short errorCode;
    private BaseConfiguration baseConfiguration;

    public static ServerLoginAnswer answerSuccess(MainServerConfiguration serverConfiguration, Account account, MicroByteBuffer buffer) {
        return new ServerLoginAnswer(serverConfiguration, account, buffer, ErrorCodes.SUCCESS);
    }

    public static ServerLoginAnswer answerFailure(MicroByteBuffer buffer, short errorCode) {
        return new ServerLoginAnswer(null, null, buffer, errorCode);
    }

    private ServerLoginAnswer(MainServerConfiguration serverConfiguration, Account account, MicroByteBuffer buffer, short errorCode) {
        this.account = account;
        this.buffer = buffer;
        this.errorCode = errorCode;
        if (serverConfiguration != null) {
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
        SimpleSerializer.serialize(errorCode, buffer);
        if (errorCode == ErrorCodes.SUCCESS) {
            ConfigurationSerializer.serialize(baseConfiguration, buffer);
            AccountSerializer.serialize(account, buffer);
        }
    }

}

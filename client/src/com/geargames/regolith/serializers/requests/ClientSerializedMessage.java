package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;

/**
 * Клиентское сщщьщение на основе байтового буффера из клиентской конфигурации.
 */
public abstract class ClientSerializedMessage extends SerializedMessage {
    private ClientConfiguration configuration;

    public ClientSerializedMessage(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    protected MicroByteBuffer getBuffer() {
        return configuration.getMessageBuffer();
    }

    protected ClientConfiguration getConfiguration() {
        return configuration;
    }
}

package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;

/**
 * User: mkutuzov
 * Date: 04.07.12
 * Ответное сообщение на запрос. Подтверждение успешности выполнения запроса.
 */
public class ClientConfirmationAnswer extends ClientDeSerializedMessage {
    private boolean confirm;

    public boolean isConfirm() {
        return confirm;
    }

    public ClientConfirmationAnswer() {
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        confirm = SimpleDeserializer.deserializeBoolean(buffer);
    }
}

package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.DeSerializedMessage;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleDeserializer;

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

    protected void deSerialize(MicroByteBuffer buffer) {
        confirm = SimpleDeserializer.deserializeBoolean(buffer);
    }
}

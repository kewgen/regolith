package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;

/**
 * User: abarakov
 * Date: 21.03.13
 * Сообщение-уведомление об отмене битвы. Рассылается всем слушателям битвы.
 */
public class ClientCancelBattleAnswer extends ClientDeSerializedMessage {
//    private boolean confirm;
//
//    public boolean isConfirm() {
//        return confirm;
//    }

    public ClientCancelBattleAnswer() {
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
//        confirm = SimpleDeserializer.deserializeBoolean(buffer);
    }
}

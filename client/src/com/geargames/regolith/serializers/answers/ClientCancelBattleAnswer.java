package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;

/**
 * User: abarakov
 * Date: 21.03.13
 * Сообщение-уведомление об отмене битвы. Рассылается всем слушателям битвы.
 */
public class ClientCancelBattleAnswer extends ClientDeSerializedMessage {
    private boolean success;

    public ClientCancelBattleAnswer() {
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        success = SimpleDeserializer.deserializeBoolean(buffer);
//        if (success) {
//            //todo: изменить механизм удаления битвы
//            ClientBattleCollection battles = ObjectManager.getInstance().getBattleCollection();
//            battles.getBattles().remove(ObjectManager.getInstance().getClientBattle());
//        }
    }

    public boolean isSuccess() {
        return success;
    }

}

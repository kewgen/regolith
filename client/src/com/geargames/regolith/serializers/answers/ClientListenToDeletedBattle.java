package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.application.ObjectManager;

/**
 * User: m.v.kutuzov
 * Date: 28.03.13
 * Удалить битву из списка созданных битв на клиенте по её идентификатору.
 */
public class ClientListenToDeletedBattle extends ClientDeSerializedMessage {

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        ObjectManager.getInstance().removeBattleById(SimpleDeserializer.deserializeInt(buffer));
    }
}

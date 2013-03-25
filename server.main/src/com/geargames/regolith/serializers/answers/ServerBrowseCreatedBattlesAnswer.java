package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;
import com.geargames.regolith.units.battle.Battle;

import java.util.LinkedList;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerBrowseCreatedBattlesAnswer extends BatchAnswer {

    public ServerBrowseCreatedBattlesAnswer(MicroByteBuffer buffer, Battle[] battles) {
        setBuffer(buffer);
        LinkedList<SerializedMessage> messages = new LinkedList<SerializedMessage>();
        setMessages(messages);
        setType(Packets.BROWSE_CREATED_BATTLES);
        for (Battle battle : battles) {
            messages.add(ServerListenToBattleAnswer.AnswerSuccess(buffer, battle, Packets.LISTEN_TO_BATTLE));
        }
    }

}

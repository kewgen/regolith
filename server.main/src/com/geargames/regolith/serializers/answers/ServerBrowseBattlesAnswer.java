package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.regolith.Packets;
import com.geargames.regolith.units.battle.Battle;

import java.util.Collection;
import java.util.LinkedList;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerBrowseBattlesAnswer extends BatchAnswer {

    public static ServerBrowseBattlesAnswer answerNew(MicroByteBuffer buffer, Collection<Battle> newBattles){
        return new ServerBrowseBattlesAnswer(buffer, newBattles, null, null);
    }

    public static ServerBrowseBattlesAnswer answerOld(MicroByteBuffer buffer, Collection<Battle> newBattles, Collection<Battle> updatedBattles, Collection<Battle> deletedBattles){
        return new ServerBrowseBattlesAnswer(buffer, newBattles, updatedBattles, deletedBattles);
    }

    private ServerBrowseBattlesAnswer(MicroByteBuffer buffer, Collection<Battle> newBattles, Collection<Battle> updatedBattles, Collection<Battle> deletedBattles) {
        setBuffer(buffer);
        LinkedList<SerializedMessage> messages = new LinkedList<SerializedMessage>();
        setMessages(messages);
        setType(Packets.BROWSE_CREATED_BATTLES);
        if (newBattles != null) {
            for (Battle battle : newBattles) {
                messages.add(ServerListenToBattleAnswer.AnswerSuccess(buffer, battle, Packets.LISTEN_TO_CREATED_BATTLE));
            }
        }
        if (updatedBattles != null) {
            for (Battle battle : updatedBattles) {
                messages.add(ServerListenToBattleAnswer.AnswerSuccess(buffer, battle, Packets.LISTEN_TO_UPDATED_BATTLE));
            }
        }
        if (deletedBattles != null) {
            for (Battle battle : deletedBattles) {
                messages.add(new ServerListenToDeletedBattleAnswer(buffer, battle));
            }
        }
    }

}

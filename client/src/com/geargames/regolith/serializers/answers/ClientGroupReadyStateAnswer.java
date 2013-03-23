package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: abarakov
 * Date: 21.03.13
 * Сообщение-ответ или оповещение о готовности/неготовности боевой группы (пользователя) к битве. Рассылается всем слушателям битвы.
 */
// ClientGroupIsReadyAnswer, ClientGroupIsNotReadyAnswer
public class ClientGroupReadyStateAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private BattleGroup battleGroup;

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        battleGroup = null;
        boolean success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            int id = SimpleDeserializer.deserializeInt(buffer);
            battleGroup = ClientBattleHelper.findBattleGroupById(battle, id);
        }
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }

    public boolean isSuccess() {
        return battleGroup != null;
    }

}
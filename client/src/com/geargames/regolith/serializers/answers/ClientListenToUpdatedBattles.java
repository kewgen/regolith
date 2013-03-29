package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.serializers.BattleDeserializer;
import com.geargames.regolith.units.battle.Battle;

/**
 * User: m.v.kutuzov
 * Date: 28.03.13
 */
public class ClientListenToUpdatedBattles extends ClientDeSerializedMessage {

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        if (SimpleDeserializer.deserializeBoolean(buffer)) {
            Battle battle = ObjectManager.getInstance().findBattleById(SimpleDeserializer.deserializeInt(buffer));
            if (battle != null) {
                buffer.setPosition(buffer.getPosition() - 4);
                BattleDeserializer.deserializeLightBattle(buffer, battle);
            }
        }
    }
}

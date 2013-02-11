package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;

/**
 * User: mkutuzov
 * Date: 05.07.12
 */
public class ClientJoinBattleAnswer extends ClientDeSerializedMessage {
    private BattleGroup battleGroup;

    private BattleAlliance alliance;

    public ClientJoinBattleAnswer(BattleAlliance battleAlliance){
        this.alliance = battleAlliance;
    }

    protected void deSerialize(MicroByteBuffer buffer) {
        if (SimpleDeserializer.deserializeBoolean(buffer)) {
            int id = SimpleDeserializer.deserializeInt(buffer);
            BattleGroupCollection groups = alliance.getAllies();
            for (int i = 0; i < groups.size(); i++) {
                if (groups.get(i).getId() == id) {
                    battleGroup = groups.get(i);
                    return;
                }
            }
            throw new IllegalStateException();
        } else {
            return;
        }
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }
}

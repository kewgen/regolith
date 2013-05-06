package com.geargames.regolith.serializers.answers;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mvkutuzov
 * Date: 18.04.13
 * Time: 11:04
 */
public class ClientNewBattleLogin extends ClientDeSerializedMessage {
    private Battle battle;
    private BattleGroup group;

    public ClientNewBattleLogin() {
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public BattleGroup getGroup() {
        return group;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        if (battle.getId() == SimpleDeserializer.deserializeInt(buffer)) {
            group = ClientBattleHelper.findBattleGroupById(battle, SimpleDeserializer.deserializeInt(buffer));
        } else {
            Debug.warning("An alien battle send this battle group login confirmation." + SimpleDeserializer.deserializeInt(buffer));
        }
    }
}

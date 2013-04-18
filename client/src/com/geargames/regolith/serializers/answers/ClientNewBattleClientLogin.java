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
 * To change this template use File | Settings | File Templates.
 */
public class ClientNewBattleClientLogin extends ClientDeSerializedMessage{
    private Battle battle;
    private BattleGroup group;

    public ClientNewBattleClientLogin() {
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        if(battle.getId() == SimpleDeserializer.deserializeInt(buffer)){
            group = ClientBattleHelper.findBattleGroupById(battle, SimpleDeserializer.deserializeInt(buffer));
        } else {
            Debug.warning("An alien battle send battle group login confirmation.");
        }
    }
}

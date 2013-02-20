package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleDeserializer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: mkutuzov
 * Date: 06.07.12
 */
public class ClientBrowseBattlesAnswer extends ClientDeSerializedMessage {
    private BaseConfiguration baseConfiguration;

    public ClientBattleCollection getBattles() {
        return ObjectManager.getInstance().getBattleCollection();
    }

    public ClientBrowseBattlesAnswer(BaseConfiguration baseConfiguration) {
        this.baseConfiguration = baseConfiguration;
    }

    protected void deSerialize(MicroByteBuffer buffer) {
        int size = SimpleDeserializer.deserializeInt(buffer);
        ClientBattleCollection battles = ObjectManager.getInstance().getBattleCollection();

        for (int i = 0; i < size; i++) {
            Battle battle = new Battle();

            battle.setId(SimpleDeserializer.deserializeInt(buffer));
            battle.setName(SimpleDeserializer.deserializeString(buffer));
            battle.setBattleType(BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), baseConfiguration));
            BattleMap map = new BattleMap();
            map.setName(SimpleDeserializer.deserializeString(buffer));
            battle.setMap(map);
            battles.add(battle);
        }
    }
}

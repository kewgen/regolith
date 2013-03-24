package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.BaseConfigurationHelper;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;
import com.geargames.regolith.units.map.BattleMap;

/**
 * Users: mkutuzov, abarakov
 * Date: 06.07.12
 */
public class ClientBrowseBattlesAnswer extends ClientDeSerializedMessage {
    private ClientConfiguration configuration;

    public ClientBrowseBattlesAnswer(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public ClientBattleCollection getBattles() {
        return ObjectManager.getInstance().getBattleCollection();
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        int size = SimpleDeserializer.deserializeInt(buffer);
        ClientBattleCollection battles = ObjectManager.getInstance().getBattleCollection();
        battles.getBattles().clear(); //todo: здесь чистится список битв

        for (int i = 0; i < size; i++) {
            Battle battle = new Battle();

            battle.setId(SimpleDeserializer.deserializeInt(buffer));
            battle.setName(SimpleDeserializer.deserializeString(buffer));
            battle.setBattleType(BaseConfigurationHelper.findBattleTypeById(SimpleDeserializer.deserializeInt(buffer), configuration.getBaseConfiguration()));
            BattleMap map = new BattleMap();
            map.setName(SimpleDeserializer.deserializeString(buffer));
            battle.setMap(map);
            battles.add(battle);
        }
    }

}

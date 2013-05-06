package com.geargames.regolith.serializers.answers;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.BattleConfiguration;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.map.observer.StrictPerimeterObserver;
import com.geargames.regolith.map.router.RecursiveWaveRouter;
import com.geargames.regolith.serializers.ConfigurationDeserializer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;

/**
 * User: mikhail v. kutuzov
 * Date: 15.08.12
 * Time: 13:41
 */
public class ClientBattleLoginAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private BattleGroup[] battleGroups;
    private boolean success;

    public ClientBattleLoginAnswer() {
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public boolean isSuccess() {
        return success;
    }

    public BattleGroup[] getBattleGroups() {
        return battleGroups;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        success = false;
        int battleId = SimpleDeserializer.deserializeInt(buffer);
        if (battle.getId() == battleId) {
            success = SimpleDeserializer.deserializeBoolean(buffer);
            if (success) {
                battleGroups = new BattleGroup[battle.getBattleType().getAllianceSize() * battle.getBattleType().getAllianceAmount()];
                int length = SimpleDeserializer.deserializeInt(buffer);
                for (int i = 0; i < length; i++) {
                    int battleGroupId = SimpleDeserializer.deserializeInt(buffer);
                    BattleGroup group = null;
                    for (BattleAlliance alliance : battle.getAlliances()) {
                        BattleGroupCollection groups = alliance.getAllies();
                        for (int j = 0; j < battle.getBattleType().getAllianceSize(); j++) {
                            if (groups.get(j).getId() == battleGroupId) {
                                group = groups.get(j);
                                break;
                            }
                        }
                        if (group != null) {
                            break;
                        }
                    }
                    battleGroups[i] = group;
                }
                ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
                configuration.setBattleConfiguration(ConfigurationDeserializer.deserializeBattleConfiguration(buffer));
            } else {
                Debug.warning("An alien battle has confirmed our login");
            }
        }
    }
}

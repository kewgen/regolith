package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.serializers.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
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

    public ClientBattleLoginAnswer(Battle battle) {
        this.battle = battle;
        battleGroups = new BattleGroup[battle.getBattleType().getAllianceSize()];
    }

    public boolean isSuccess() {
        return success;
    }

    public BattleGroup[] getBattleGroups() {
        return battleGroups;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) {
        if (battle.getId() != SimpleDeserializer.deserializeInt(buffer)) {
            success = SimpleDeserializer.deserializeBoolean(buffer);
            if (success) {
                int size = SimpleDeserializer.deserializeInt(buffer);
                for (int i = 0; i < size; i++) {
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
            }
        }
    }
}

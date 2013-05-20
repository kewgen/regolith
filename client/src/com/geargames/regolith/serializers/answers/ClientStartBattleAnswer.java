package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.serializers.BattleDeserializer;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.dictionaries.BattleGroupCollection;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 05.07.12
 */
public class ClientStartBattleAnswer extends ClientDeSerializedMessage {
    private Battle battle;
    private String host;
    private int port;
    private boolean success;

    public ClientStartBattleAnswer() {
        this.success = false;
    }

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        host = null;
        port = 0;
        success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            battle = ObjectManager.getInstance().getClientBattle();
            ClientConfiguration configuration = ClientConfigurationFactory.getConfiguration();
            BattleDeserializer.deserializeBattle(buffer, configuration.getBaseConfiguration(), configuration.getAccount(), battle);
            BattleAlliance[] alliances = battle.getAlliances();
            int allianceSize = alliances.length;
            for (int i = 0; i < allianceSize; i++) {
                BattleGroupCollection groups = alliances[i].getAllies();
                int groupsAmount = groups.size();
                for (int j = 0; j < groupsAmount; j++) {
                    WarriorCollection warriors = groups.get(j).getWarriors();
                    int warriorsAmount = warriors.size();
                    for (int k = 0; k < warriorsAmount; k++) {
                        ClientWarriorCollection collection = new ClientWarriorCollection();
                        collection.setWarriors(new Vector());
                        warriors.get(k).setDetectedEnemies(collection);
                    }
                }
            }
            host = SimpleDeserializer.deserializeString(buffer);
            port = SimpleDeserializer.deserializeInt(buffer);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Battle getBattle() {
        return battle;
    }

    public boolean isSuccess() {
        return success;
    }

}

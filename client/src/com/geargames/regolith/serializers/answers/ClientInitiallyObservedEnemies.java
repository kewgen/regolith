package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.dictionaries.WarriorCollection;

import java.util.Vector;

/**
 * User: mvkutuzov
 * Date: 26.04.13
 * Time: 13:32
 */
public class ClientInitiallyObservedEnemies extends ClientDeSerializedMessage {
    private Battle battle;

    private ClientWarriorCollection enemies;


    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    /**
     * Вернуть список видимых вражеских бойцов.
     *
     * @return
     */
    public WarriorCollection getEnemies() {
        return enemies;
    }


    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        int size = SimpleDeserializer.deserializeInt(buffer);
        enemies = new ClientWarriorCollection();
        enemies.setWarriors(new Vector(size));
        for (int i = 0; i < size; i++) {
            Warrior warrior = ClientBattleHelper.findWarrior(battle, SimpleDeserializer.deserializeInt(buffer));
            WarriorHelper.putWarriorIntoMap(warrior, battle.getMap(), SimpleDeserializer.deserializeInt(buffer), SimpleDeserializer.deserializeInt(buffer));
            enemies.add(warrior);
        }
    }
}

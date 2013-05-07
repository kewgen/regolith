package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

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
    public ClientWarriorCollection getEnemies() {
        return enemies;
    }


    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        ClientWarriorCollection enemyUnits = PRegolithPanelManager.getInstance().getBattleScreen().getEnemyUnits();

        byte size = buffer.get();
        enemies = new ClientWarriorCollection();
        enemies.setWarriors(new Vector(size));
        for (int i = 0; i < size; i++) {
            int enemyId = SimpleDeserializer.deserializeInt(buffer);
            Warrior unit = ClientBattleHelper.getWarriorElementById(enemyUnits, enemyId);
            short cellX = SimpleDeserializer.deserializeShort(buffer);
            short cellY = SimpleDeserializer.deserializeShort(buffer);
            WarriorHelper.putWarriorIntoMap(battle.getMap().getCells(), unit, cellX, cellY);
            enemies.add(unit);
        }
    }

}

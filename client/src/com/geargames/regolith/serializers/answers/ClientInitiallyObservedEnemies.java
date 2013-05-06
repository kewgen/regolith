package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.ClientHumanElementCollection;
import com.geargames.regolith.units.map.HumanElement;

import java.util.Vector;

/**
 * User: mvkutuzov
 * Date: 26.04.13
 * Time: 13:32
 */
public class ClientInitiallyObservedEnemies extends ClientDeSerializedMessage {
    private Battle battle;

    private ClientHumanElementCollection enemies;

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
    public ClientHumanElementCollection getEnemies() {
        return enemies;
    }


    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        ClientHumanElementCollection enemyUnits = PRegolithPanelManager.getInstance().getBattleScreen().getEnemyUnits();

        byte size = buffer.get();
        enemies = new ClientHumanElementCollection();
        enemies.setElements(new Vector(size));
        for (int i = 0; i < size; i++) {
            int enemyId = SimpleDeserializer.deserializeInt(buffer);
            HumanElement unit = BattleMapHelper.getHumanElementByHumanId(enemyUnits, enemyId);
            short x = SimpleDeserializer.deserializeShort(buffer);
            short y = SimpleDeserializer.deserializeShort(buffer);
            WarriorHelper.putWarriorIntoMap(battle.getMap().getCells(), unit, x, y);
            enemies.add(unit);
        }
    }

}

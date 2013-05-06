package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

import java.util.Vector;

/**
 * User: mvkutuzov
 * Date: 23.04.13
 * Time: 13:38
 * Пришёл ответ на запрос о ходе, на данный момент warriorId приходящий вторым параметром не анализируется.
 */
public class ClientMoveWarriorAnswer extends ClientDeSerializedMessage {
    private short x;
    private short y;
    private ClientWarriorCollection enemies;

    private boolean success;

    private Battle battle;

    public ClientWarriorCollection getEnemies() {
        return enemies;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public boolean isSuccess() {
        return success;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            SimpleDeserializer.deserializeInt(buffer);
            x = SimpleDeserializer.deserializeShort(buffer);
            y = SimpleDeserializer.deserializeShort(buffer);
            int size = SimpleDeserializer.deserializeInt(buffer);
            enemies = new ClientWarriorCollection();
            enemies.setWarriors(new Vector(size));
            for (int j = 0; j < size; j++) {
                BattleAlliance enemyAlliance = battle.getAlliances()[buffer.get()];
                BattleGroup enemyGroup = ClientBattleHelper.findBattleGroupInAllianceById(enemyAlliance, SimpleDeserializer.deserializeInt(buffer));
                Warrior enemy = ClientBattleHelper.findWarriorInBattleGroup(enemyGroup, SimpleDeserializer.deserializeInt(buffer));
                int xx = SimpleDeserializer.deserializeShort(buffer);
                int yy = SimpleDeserializer.deserializeShort(buffer);
                WarriorHelper.putWarriorIntoMap(enemy, battle.getMap(), xx, yy);
                enemies.add(enemy);
            }
        }
    }
}

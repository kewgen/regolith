package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mvkutuzov
 * Date: 23.04.13
 * Time: 13:38
 * Пришёл ответ на запрос о ходе, на данный момент warriorId приходящий вторым параметром не анализируется.
 */
public class ClientMoveWarriorAnswer extends ClientDeSerializedMessage {
    private short x;
    private short y;
    private ArrayList enemiesOnTheHorizon;
    private boolean success;

    private Battle battle;
    private ArrayList enemies;

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public ArrayList getEnemiesOnTheHorizon() {
        return enemiesOnTheHorizon;
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

    public ArrayList getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList enemies) {
        this.enemies = enemies;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        success = SimpleDeserializer.deserializeBoolean(buffer);
        if (success) {
            SimpleDeserializer.deserializeInt(buffer);
            x = SimpleDeserializer.deserializeShort(buffer);
            y = SimpleDeserializer.deserializeShort(buffer);
            int size = SimpleDeserializer.deserializeInt(buffer);
            enemiesOnTheHorizon = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                int warriorId = SimpleDeserializer.deserializeInt(buffer);
                BattleUnit unit = ClientBattleHelper.findBattleUnitByWarriorId(enemies, warriorId);
                int xx = SimpleDeserializer.deserializeShort(buffer);
                int yy = SimpleDeserializer.deserializeShort(buffer);
                WarriorHelper.putWarriorIntoMap(unit.getUnit().getWarrior(), battle.getMap(), xx, yy);
            }
        }
    }
}

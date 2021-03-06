package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Direction;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

import java.util.Vector;

/**
 * Users: mvkutuzov, abarakov
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
            int id = SimpleDeserializer.deserializeInt(buffer);
            x = SimpleDeserializer.deserializeShort(buffer);
            y = SimpleDeserializer.deserializeShort(buffer);
            byte size = buffer.get();
            enemies = new ClientWarriorCollection();
            enemies.setWarriors(new Vector(size));
            ClientWarriorCollection enemyUnits = ClientConfigurationFactory.getConfiguration().getBattleContext().getEnemyUnits();
            for (int i = 0; i < size; i++) {
                int warriorId = SimpleDeserializer.deserializeInt(buffer);
                Warrior warrior = ClientBattleHelper.getWarriorById(enemyUnits, warriorId);
                int xx = SimpleDeserializer.deserializeShort(buffer);
                int yy = SimpleDeserializer.deserializeShort(buffer);
                int direction = SimpleDeserializer.deserializeInt(buffer);
                WarriorHelper.putWarriorIntoMap(battle.getMap().getCells(), warrior, xx, yy);
                warrior.setDirection(Direction.getByNumber(direction));
                //todo-asap: установить значение свойству Sitting
                enemies.add(warrior);
            }
        }
    }

}

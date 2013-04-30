package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;

import java.util.Vector;

/**
 * User: mvkutuzov
 * Date: 23.04.13
 * Time: 15:54
 * Асинхронное сообщение о ходе одного из союзников.
 *
 */
public class ClientMoveAllyAnswer extends ClientDeSerializedMessage {
    private BattleAlliance alliance;

    private ClientWarriorCollection enemies;
    private Warrior ally;
    private short x;
    private short y;

    /**
     * Что за союзник сделал ход.
     *
     * @return
     */
    public Warrior getAlly() {
        return ally;
    }

    /**
     * Куда он перешёл по x.
     *
     * @return
     */
    public short getX() {
        return x;
    }

    /**
     * Куда он перешёл по y.
     *
     * @return
     */
    public short getY() {
        return y;
    }

    public ClientWarriorCollection getEnemies() {
        return enemies;
    }

    /**
     * Вернуть боевой союз, именно его боец сделает ход.
     *
     * @return
     */
    public BattleAlliance getAlliance() {
        return alliance;
    }

    public void setAlliance(BattleAlliance alliance) {
        this.alliance = alliance;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        Battle battle = alliance.getBattle();
        int groupId = SimpleDeserializer.deserializeInt(buffer);
        int warriorId = SimpleDeserializer.deserializeInt(buffer);
        x = SimpleDeserializer.deserializeShort(buffer);
        y = SimpleDeserializer.deserializeShort(buffer);

        BattleGroup group = ClientBattleHelper.findBattleGroupInAllianceById(alliance, groupId);
        ally = ClientBattleHelper.findWarriorInBattleGroup(group, warriorId);
        int size = SimpleDeserializer.deserializeInt(buffer);
        enemies = new ClientWarriorCollection();
        enemies.setWarriors(new Vector(size));
        for (int j = 0; j < size; j++) {
            BattleAlliance enemyAlliance = battle.getAlliances()[buffer.get()];
            BattleGroup enemyGroup = ClientBattleHelper.findBattleGroupInAllianceById(enemyAlliance, SimpleDeserializer.deserializeInt(buffer));
            Warrior enemy = ClientBattleHelper.findWarriorInBattleGroup(enemyGroup, SimpleDeserializer.deserializeInt(buffer));
            int xx = SimpleDeserializer.deserializeInt(buffer);
            int yy = SimpleDeserializer.deserializeInt(buffer);
            WarriorHelper.putWarriorIntoMap(enemy, battle.getMap(), xx, yy);
            enemies.add(enemy);
        }
    }
}

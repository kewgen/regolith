package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.ClientBattleContext;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.ClientWarriorElement;

import java.util.Vector;

/**
 * Users: mvkutuzov, abarakov
 * Date: 23.04.13
 * Time: 15:54
 * Асинхронное сообщение о ходе одного из союзников.
 */
public class ClientMoveAllyAnswer extends ClientDeSerializedMessage {
    private BattleAlliance alliance;

    private ClientWarriorCollection enemies;
    private ClientWarriorElement ally;
    private short x;
    private short y;

    /**
     * Что за союзник сделал ход.
     *
     * @return
     */
    public ClientWarriorElement getAlly() {
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
        int allyId = SimpleDeserializer.deserializeInt(buffer);
        x = SimpleDeserializer.deserializeShort(buffer);
        y = SimpleDeserializer.deserializeShort(buffer);

        ClientBattleContext battleContext = ClientConfigurationFactory.getConfiguration().getBattleContext();
        ClientWarriorCollection allyUnits = battleContext.getAllyUnits();
        ClientWarriorCollection enemyUnits = battleContext.getEnemyUnits();

        ally = (ClientWarriorElement) ClientBattleHelper.getWarriorById(allyUnits, allyId);

        byte size = buffer.get();
        enemies = new ClientWarriorCollection();
        enemies.setWarriors(new Vector(size));
        for (int j = 0; j < size; j++) {
            int enemyId = SimpleDeserializer.deserializeInt(buffer);
            ClientWarriorElement enemy = (ClientWarriorElement) ClientBattleHelper.getWarriorById(enemyUnits, enemyId);
            int xx = SimpleDeserializer.deserializeInt(buffer);
            int yy = SimpleDeserializer.deserializeInt(buffer);
            WarriorHelper.putWarriorIntoMap(battle.getMap().getCells(), enemy, xx, yy);
            enemies.add(enemy);
        }
    }

}

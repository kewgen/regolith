package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleAlliance;
import com.geargames.regolith.units.dictionaries.ClientHumanElementCollection;
import com.geargames.regolith.units.map.ClientHumanElement;

import java.util.Vector;

/**
 * Users: mvkutuzov, abarakov
 * Date: 23.04.13
 * Time: 15:54
 * Асинхронное сообщение о ходе одного из союзников.
 */
public class ClientMoveAllyAnswer extends ClientDeSerializedMessage {
    private BattleAlliance alliance;

    private ClientHumanElementCollection enemies;
    private ClientHumanElement ally;
    private short x;
    private short y;

    /**
     * Что за союзник сделал ход.
     *
     * @return
     */
    public ClientHumanElement getAlly() {
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

    public ClientHumanElementCollection getEnemies() {
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

        BattleScreen battleScreen = PRegolithPanelManager.getInstance().getBattleScreen();
        ClientHumanElementCollection allyUnits = battleScreen.getAllyUnits();
        ClientHumanElementCollection enemyUnits = battleScreen.getEnemyUnits();

        ally = (ClientHumanElement) BattleMapHelper.getHumanElementByHumanId(allyUnits, allyId);

        byte size = buffer.get();
        enemies = new ClientHumanElementCollection();
        enemies.setElements(new Vector(size));
        for (int j = 0; j < size; j++) {
            int enemyId = SimpleDeserializer.deserializeInt(buffer);
            ClientHumanElement enemy = (ClientHumanElement) BattleMapHelper.getHumanElementByHumanId(enemyUnits, enemyId);
            int xx = SimpleDeserializer.deserializeInt(buffer);
            int yy = SimpleDeserializer.deserializeInt(buffer);
            WarriorHelper.putWarriorIntoMap(battle.getMap().getCells(), enemy, xx, yy);
            enemies.add(enemy);
        }
    }

}

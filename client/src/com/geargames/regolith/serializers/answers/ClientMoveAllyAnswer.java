package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.dictionaries.ClientHumanElementCollection;
import com.geargames.regolith.units.map.HumanElement;

/**
 * User: mvkutuzov
 * Date: 23.04.13
 * Time: 15:54
 * Асинхронное сообщение о ходе одного из союзников. Для работы необходимо передать список дружественных BattleUnit
 * в вызове setAllies.
 */
public class ClientMoveAllyAnswer extends ClientDeSerializedMessage {
    private HumanElement ally;
    private int x;
    private int y;

    private BattleScreen battleScreen;

    /**
     * Что за союзник сделал ход.
     *
     * @return
     */
    public HumanElement getAlly() {
        return ally;
    }

    /**
     * Куда он перешёл по x.
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Куда он перешёл по y.
     *
     * @return
     */
    public int getY() {
        return y;
    }


    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        int warriorId = SimpleDeserializer.deserializeInt(buffer);
        x = SimpleDeserializer.deserializeShort(buffer);
        y = SimpleDeserializer.deserializeShort(buffer);
        ally = null;
        ClientHumanElementCollection allies = battleScreen.getAllyUnits();
        ClientHumanElementCollection enemies = battleScreen.getEnemyUnits();
        for (int i = 0; i < allies.size(); i++) {
            HumanElement tmp = allies.get(i);
            if (tmp.getHuman().getId() == warriorId) {
                ally = tmp;
                break;
            }
        }
        int size = SimpleDeserializer.deserializeInt(buffer);
        for (int j = 0; j < size; j++) {
            int humanId = SimpleDeserializer.deserializeInt(buffer);
            HumanElement enemy = BattleMapHelper.getHumanElementByHumanId(enemies, humanId);
            short cellX = SimpleDeserializer.deserializeShort(buffer);
            short cellY = SimpleDeserializer.deserializeShort(buffer);
            battleScreen.putEnemyInPosition(enemy, cellX, cellY);
        }
    }

}

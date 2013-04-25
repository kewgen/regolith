package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.Unit;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.finder.ProjectionFinder;
import com.geargames.regolith.units.map.finder.ReverseProjectionFinder;

/**
 * User: mvkutuzov
 * Date: 23.04.13
 * Time: 15:54
 * Асинхронное сообщение о ходе одного из союзников. Для работы необходимо передать список дружественных BattleUnit
 * в вызове setAllies.
 */
public class ClientMoveAllyAnswer extends ClientDeSerializedMessage {
    private BattleUnit ally;
    private int x;
    private int y;

    private BattleScreen battleScreen;

    /**
     * Что за союзник сделал ход.
     * @return
     */
    public BattleUnit getAlly() {
        return ally;
    }

    /**
     * Куда он перешёл по x.
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Куда он перешёл по y.
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
        ArrayList allies = battleScreen.getAllies();
        ArrayList enemies = battleScreen.getEnemies();
        for (int i = 0; i < allies.size(); i++) {
            BattleUnit tmp = (BattleUnit) allies.get(i);
            if (tmp.getUnit().getWarrior().getId() == warriorId) {
                ally = tmp;
                int size = SimpleDeserializer.deserializeInt(buffer);
                for(int j = 0; j < size; j++){
                    BattleUnit enemy = ClientBattleHelper.findBattleUnitByWarriorId(enemies, SimpleDeserializer.deserializeInt(buffer));
                    battleScreen.putEnemyInPosition(enemy, SimpleDeserializer.deserializeShort(buffer), SimpleDeserializer.deserializeShort(buffer));
                }
                break;
            }
        }
    }
}

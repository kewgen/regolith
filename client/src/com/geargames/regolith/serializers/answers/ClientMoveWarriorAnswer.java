package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.ClientHumanElementCollection;
import com.geargames.regolith.units.map.HumanElement;

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
    private ClientHumanElementCollection enemies;

    private boolean success;

    private Battle battle;

    public ClientHumanElementCollection getEnemies() {
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
            byte size = buffer.get();
            enemies = new ClientHumanElementCollection();
            enemies.setElements(new Vector(size));
            BattleScreen battleScreen = PRegolithPanelManager.getInstance().getBattleScreen();
            ClientHumanElementCollection groupUnits = battleScreen.getGroupUnits();
            for (int j = 0; j < size; j++) {
                int warriorId = SimpleDeserializer.deserializeInt(buffer);
                HumanElement unit = BattleMapHelper.getHumanElementByHumanId(groupUnits, warriorId);
                int xx = SimpleDeserializer.deserializeShort(buffer);
                int yy = SimpleDeserializer.deserializeShort(buffer);
                WarriorHelper.putWarriorIntoMap(battle.getMap().getCells(), unit, xx, yy);
                enemies.add(unit);
            }
        }
    }

}

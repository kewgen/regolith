package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.dictionaries.ClientHumanElementCollection;
import com.geargames.regolith.units.map.HumanElement;

/**
 * User: mvkutuzov
 * Date: 23.04.13
 * Time: 13:38
 */
public class ClientMoveMyWarriorAnswer extends ClientDeSerializedMessage {
    private short x;
    private short y;
    private ArrayList enemiesOnTheHorizont;

    private HumanElement humanElement;
    private ClientHumanElementCollection enemies;

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public ArrayList getEnemiesOnTheHorizont() {
        return enemiesOnTheHorizont;
    }

    public HumanElement getHumanElement() {
        return humanElement;
    }

    public void setHumanElement(HumanElement humanElement) {
        this.humanElement = humanElement;
    }

    public ClientHumanElementCollection getEnemies() {
        return enemies;
    }

    public void setEnemies(ClientHumanElementCollection enemies) {
        this.enemies = enemies;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        int warriorId = SimpleDeserializer.deserializeInt(buffer);
        if (humanElement.getHuman().getId() != warriorId) {
            throw new Exception("A warrior was not valid (actual = " + warriorId + "; expected = " + humanElement.getHuman().getId() + ")");
        }
        x = SimpleDeserializer.deserializeShort(buffer);
        y = SimpleDeserializer.deserializeShort(buffer);
        int size = SimpleDeserializer.deserializeInt(buffer);
        enemiesOnTheHorizont = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            warriorId = SimpleDeserializer.deserializeInt(buffer);
            HumanElement unit = BattleMapHelper.getHumanElementByHumanId(enemies, warriorId);
            int xx = SimpleDeserializer.deserializeShort(buffer);
            int yy = SimpleDeserializer.deserializeShort(buffer);
            WarriorHelper.putWarriorIntoMap(humanElement.getHuman().getBattleGroup().getAlliance().getBattle().getMap().getCells(), unit, xx, yy);
        }
    }

}

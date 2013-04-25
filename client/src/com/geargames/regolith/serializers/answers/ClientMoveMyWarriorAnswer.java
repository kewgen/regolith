package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.BattleUnit;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mvkutuzov
 * Date: 23.04.13
 * Time: 13:38
 */
public class ClientMoveMyWarriorAnswer extends ClientDeSerializedMessage {
    private short x;
    private short y;
    private ArrayList enemiesOnTheHorizont;

    private Warrior warrior;
    private ArrayList enemies;

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public ArrayList getEnemiesOnTheHorizont() {
        return enemiesOnTheHorizont;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    public ArrayList getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList enemies) {
        this.enemies = enemies;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        int warriorId = SimpleDeserializer.deserializeInt(buffer);
        if (warrior.getId() != warriorId) {
            throw new Exception("A warrior was not valid ( actual = " + warriorId + "; expected  = " + warrior.getId());
        }
        x = SimpleDeserializer.deserializeShort(buffer);
        y = SimpleDeserializer.deserializeShort(buffer);
        int size = SimpleDeserializer.deserializeInt(buffer);
        enemiesOnTheHorizont = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            warriorId = SimpleDeserializer.deserializeInt(buffer);
            BattleUnit unit = ClientBattleHelper.findBattleUnitByWarriorId(enemies, warriorId);
            int xx = SimpleDeserializer.deserializeShort(buffer);
            int yy = SimpleDeserializer.deserializeShort(buffer);
            WarriorHelper.putWarriorIntoMap(unit.getUnit().getWarrior(), warrior.getBattleGroup().getAlliance().getBattle().getMap(), xx, yy);
        }
    }
}

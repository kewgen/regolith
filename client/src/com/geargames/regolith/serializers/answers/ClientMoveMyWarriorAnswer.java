package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mvkutuzov
 * Date: 23.04.13
 * Time: 13:38
 */
public class ClientMoveMyWarriorAnswer extends ClientDeSerializedMessage {
    private short x;
    private short y;
    private Warrior warrior;

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        int warriorId = SimpleDeserializer.deserializeInt(buffer);
        short x = SimpleDeserializer.deserializeShort(buffer);
        short y = SimpleDeserializer.deserializeShort(buffer);
        if(warrior.getId() != warriorId){
            throw new Exception("A warrior was not valid ( actual = " + warriorId + "; expected  = " + warrior.getId());
        }
    }
}

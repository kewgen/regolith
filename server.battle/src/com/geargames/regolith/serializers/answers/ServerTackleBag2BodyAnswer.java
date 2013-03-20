package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.serializers.SerializeHelper;
import com.geargames.regolith.serializers.TackleSerializer;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mikhail v. kutuzov
 * Date: 28.09.12
 * Time: 23:42
 */
public class ServerTackleBag2BodyAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private StateTackle tackle;

    public ServerTackleBag2BodyAnswer(MicroByteBuffer buffer, Warrior warrior, StateTackle tackle) {
        this.buffer = buffer;
        this.warrior = warrior;
        this.tackle = tackle;
    }

    @Override
    public short getType() {
        return Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_BODY;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SerializeHelper.serializeEntityReference(warrior, buffer);
        if(tackle instanceof Weapon){
            TackleSerializer.serializeWeapon((Weapon)tackle, buffer);
        }else{
            TackleSerializer.serializeArmor((Armor)tackle, buffer);
        }
    }
}

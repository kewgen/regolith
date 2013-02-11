package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.serializers.TackleSerializer;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.StateTackle;
import com.geargames.regolith.units.tackle.Weapon;

/**
 * User: mikhail v. kutuzov
 * Date: 20.08.12
 * Time: 17:22
 */
public class ServerTackleBag2BoxAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private StateTackle stateTackle;
    private short x;
    private short y;

    public ServerTackleBag2BoxAnswer(MicroByteBuffer buffer, Warrior warrior, StateTackle stateTackle, short x, short y) {
        this.buffer = buffer;
        this.warrior = warrior;
        this.stateTackle = stateTackle;
        this.x = x;
        this.y = y;
    }

    @Override
    public short getType() {
        return Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_BOX;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(warrior, buffer);
        if (stateTackle instanceof Weapon) {
            TackleSerializer.serializeWeapon((Weapon) stateTackle, buffer);
        } else {
            TackleSerializer.serializeArmor((Armor) stateTackle, buffer);
        }
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}

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
 * Time: 19:03
 */
public class ServerTackleBag2GroundAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private StateTackle tackle;
    private short x;
    private short y;
    private boolean success;

    public static ServerTackleBag2GroundAnswer answerFailure(MicroByteBuffer buffer){
        return new ServerTackleBag2GroundAnswer(buffer, null, null, (short)0, (short)0, false);
    }

    public static ServerTackleBag2GroundAnswer answerSuccess(MicroByteBuffer buffer, Warrior warrior, StateTackle stateTackle, short x, short y){
        return new ServerTackleBag2GroundAnswer(buffer, warrior, stateTackle, x, y, true);
    }


    private ServerTackleBag2GroundAnswer(MicroByteBuffer buffer, Warrior warrior, StateTackle tackle, short x, short y, boolean success) {
        this.buffer = buffer;
        this.warrior = warrior;
        this.tackle = tackle;
        this.x = x;
        this.y = y;
        this.success = success;
    }

    @Override
    public short getType() {
        return Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_GROUND;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SimpleSerializer.serializeEntityReference(warrior, buffer);
            if (tackle instanceof Weapon) {
                TackleSerializer.serializeWeapon((Weapon) tackle, buffer);
            } else {
                TackleSerializer.serializeArmor((Armor) tackle, buffer);
            }
            SimpleSerializer.serialize(x, buffer);
            SimpleSerializer.serialize(y, buffer);
        }
    }
}

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
 * Date: 17.08.12
 * Time: 17:30
 */
public class ServerTackleBody2BoxAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private StateTackle tackle;
    private Warrior warrior;
    private short x;
    private short y;

    public ServerTackleBody2BoxAnswer(MicroByteBuffer buffer, StateTackle tackle, Warrior warrior, short x, short y) {
        this.buffer = buffer;
        this.tackle = tackle;
        this.warrior = warrior;
        this.x = x;
        this.y = y;
    }

    @Override
    public short getType() {
        return Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BOX;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(warrior.getBattleGroup().getAccount(), buffer);
        SimpleSerializer.serializeEntityReference(warrior.getBattleGroup().getAlliance(), buffer);
        SimpleSerializer.serializeEntityReference(warrior.getBattleGroup(), buffer);
        SimpleSerializer.serializeEntityReference(warrior, buffer);
        if (tackle instanceof Weapon) {
            SimpleSerializer.serialize(SimpleSerializer.findTypeId("Weapon"), buffer);
            TackleSerializer.serializeWeapon((Weapon) tackle, buffer);
        } else {
            SimpleSerializer.serialize(SimpleSerializer.findTypeId("Armor"), buffer);
            TackleSerializer.serializeArmor((Armor) tackle, buffer);
        }

        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}

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
 * Time: 22:22
 */
public class ServerTackleBody2GroundAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior warrior;
    private StateTackle stateTackle;
    private short x;
    private short y;

    public ServerTackleBody2GroundAnswer(MicroByteBuffer buffer, Warrior warrior, StateTackle stateTackle, short x, short y) {
        this.buffer = buffer;
        this.warrior = warrior;
        this.stateTackle = stateTackle;
        this.x = x;
        this.y = y;
    }

    @Override
    public short getType() {
        return Packets.TAKE_TACKLE_FROM_BODY_PUT_ON_GROUND;
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
        if (stateTackle instanceof Weapon) {
            SimpleSerializer.serialize(SimpleSerializer.findTypeId("Weapon"), buffer);
            TackleSerializer.serializeWeapon((Weapon) stateTackle, buffer);
        } else {
            SimpleSerializer.serialize(SimpleSerializer.findTypeId("Armor"), buffer);
            TackleSerializer.serializeArmor((Armor) stateTackle, buffer);
        }
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}

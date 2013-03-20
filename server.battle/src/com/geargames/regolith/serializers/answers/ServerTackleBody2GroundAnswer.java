package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.*;
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
        SerializeHelper.serializeEntityReference(warrior.getBattleGroup().getAccount(), buffer);
        SerializeHelper.serializeEntityReference(warrior.getBattleGroup().getAlliance(), buffer);
        SerializeHelper.serializeEntityReference(warrior.getBattleGroup(), buffer);
        SerializeHelper.serializeEntityReference(warrior, buffer);
        if (stateTackle instanceof Weapon) {
            SimpleSerializer.serialize(SerializeHelper.findTypeId("Weapon"), buffer);
            TackleSerializer.serializeWeapon((Weapon) stateTackle, buffer);
        } else {
            SimpleSerializer.serialize(SerializeHelper.findTypeId("Armor"), buffer);
            TackleSerializer.serializeArmor((Armor) stateTackle, buffer);
        }
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
    }
}

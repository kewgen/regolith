package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.Warrior;

/**
 * Created with IntelliJ IDEA.
 * User: olga
 * Date: 24.07.12
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class ServerShootAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private Warrior hunter;
    private Warrior victim;

    public ServerShootAnswer(MicroByteBuffer buffer, Warrior hunter, Warrior victim) {
        this.buffer = buffer;
        this.hunter = hunter;
        this.victim = victim;
    }

    @Override
    public short getType() {
        return Packets.SHOOT;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        if (hunter != null) {
            SimpleSerializer.serializeEntityReference(hunter, buffer);
            SimpleSerializer.serialize(hunter.getHealth(), buffer);
            SimpleSerializer.serialize(hunter.getWeapon().getState(), buffer);
            SimpleSerializer.serialize(hunter.getWeapon().getLoad(), buffer);
            SimpleSerializer.serialize(hunter.getHeadArmor().getState(), buffer);
            SimpleSerializer.serialize(hunter.getTorsoArmor().getState(), buffer);
            SimpleSerializer.serialize(hunter.getLegsArmor().getState(), buffer);
            SimpleSerializer.serialize(hunter.getWeapon().getLoad(), buffer);
        } else {
            SimpleSerializer.serialize(SimpleSerializer.NULL_REFERENCE, buffer);
        }
        if (victim != null) {
            SimpleSerializer.serializeEntityReference(victim, buffer);
            SimpleSerializer.serialize(victim.getHealth(), buffer);
            SimpleSerializer.serialize(victim.getHeadArmor().getState(), buffer);
            SimpleSerializer.serialize(victim.getTorsoArmor().getState(), buffer);
            SimpleSerializer.serialize(victim.getLegsArmor().getState(), buffer);
        } else {
            SimpleSerializer.serialize(SimpleSerializer.NULL_REFERENCE, buffer);
        }
    }
}

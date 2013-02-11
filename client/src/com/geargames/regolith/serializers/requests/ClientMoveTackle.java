package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 * Запрос на перемещение вещи надетой на бойца.
 */
public class ClientMoveTackle extends ClientSerializedMessage {
    private Warrior warrior;
    private StateTackle tackle;
    private short type;

    public ClientMoveTackle(ClientConfiguration configuration) {
        super(configuration);
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    public StateTackle getTackle() {
        return tackle;
    }

    public void setTackle(StateTackle tackle) {
        this.tackle = tackle;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getType() {
        return type;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serializeEntityReference(warrior, buffer);
        SimpleSerializer.serializeEntityReference(tackle, buffer);
    }

}

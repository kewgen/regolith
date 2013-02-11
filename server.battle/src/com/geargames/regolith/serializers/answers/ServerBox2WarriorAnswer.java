package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.tackle.*;
import com.geargames.regolith.units.battle.Warrior;

/**
 * User: mkutuzov
 * Date: 25.07.12
 */
public class ServerBox2WarriorAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private short type;
    private short boxX;
    private short boxY;
    private Element element;


    public ServerBox2WarriorAnswer(MicroByteBuffer buffer, short type, short boxX, short boxY, Element element) {
        this.buffer = buffer;
        this.type = type;
        this.element = element;
        this.boxX = boxX;
        this.boxY = boxY;
    }

    @Override
    public short getType() {
        return type;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(boxX, buffer);
        SimpleSerializer.serialize(boxY, buffer);
        SimpleSerializer.serializeEntityReference(element, buffer);
    }
}

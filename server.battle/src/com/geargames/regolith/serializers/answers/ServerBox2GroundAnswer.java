package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.battle.Box;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.AbstractTackle;

/**
 * User: mikhail v. kutuzov
 * Date: 17.09.12
 * Time: 11:54
 */
public class ServerBox2GroundAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private short type;
    private short boxX;
    private short boxY;
    private Element element;
    private short x;
    private short y;

    public ServerBox2GroundAnswer(MicroByteBuffer buffer, short type, short boxX, short boxY, Element element, short x, short y) {
        this.buffer = buffer;
        this.type = type;
        this.boxX = boxX;
        this.boxY = boxY;
        this.element = element;
        this.x = x;
        this.y = y;
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
        SimpleSerializer.serialize(x, buffer);
        SimpleSerializer.serialize(y, buffer);
        SimpleSerializer.serializeEntityReference(element, buffer);
    }
}

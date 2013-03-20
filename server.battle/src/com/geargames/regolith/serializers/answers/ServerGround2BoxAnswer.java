package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;

/**
 * User: mikhail v. kutuzov
 * Date: 28.09.12
 * Time: 17:27
 */
public class ServerGround2BoxAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private short groundX;
    private short groundY;
    private short boxX;
    private short boxY;
    private short type;

    public ServerGround2BoxAnswer(MicroByteBuffer buffer, short groundX, short groundY, short boxX, short boxY, short type) {
        this.buffer = buffer;
        this.groundX = groundX;
        this.groundY = groundY;
        this.boxX = boxX;
        this.boxY = boxY;
        this.type = type;
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
        SimpleSerializer.serialize(groundX, buffer);
        SimpleSerializer.serialize(groundY, buffer);
        SimpleSerializer.serialize(boxX, buffer);
        SimpleSerializer.serialize(boxY, buffer);
    }
}

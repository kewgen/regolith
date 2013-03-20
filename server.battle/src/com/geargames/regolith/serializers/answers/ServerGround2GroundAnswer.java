package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;

/**
 * User: mikhail v. kutuzov
 * Date: 25.09.12
 * Time: 13:33
 */
public class ServerGround2GroundAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;

    private short fromX;
    private short fromY;
    private short toX;
    private short toY;
    private short type;

    public ServerGround2GroundAnswer(MicroByteBuffer buffer, short fromX, short fromY, short toX, short toY, short type) {
        this.buffer = buffer;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
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
        SimpleSerializer.serialize(fromX, buffer);
        SimpleSerializer.serialize(fromY, buffer);
        SimpleSerializer.serialize(toX, buffer);
        SimpleSerializer.serialize(toY, buffer);
    }

}

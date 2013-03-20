package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;

/**
 * User: mikhail v. kutuzov
 * Date: 25.09.12
 * Time: 16:35
 */
public class ServerMedikitGround2BoxAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private short fromX;
    private short fromY;
    private short boxX;
    private short boxY;
    private short position;

    public ServerMedikitGround2BoxAnswer(MicroByteBuffer buffer, short fromX, short fromY, short boxX, short boxY, short position) {
        this.buffer = buffer;
        this.fromX = fromX;
        this.fromY = fromY;
        this.boxX = boxX;
        this.boxY = boxY;
        this.position = position;
    }

    @Override
    public short getType() {
        return Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BOX;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(fromX, buffer);
        SimpleSerializer.serialize(fromY, buffer);
        SimpleSerializer.serialize(boxX, buffer);
        SimpleSerializer.serialize(boxY, buffer);
        SimpleSerializer.serialize(position, buffer);
    }
}

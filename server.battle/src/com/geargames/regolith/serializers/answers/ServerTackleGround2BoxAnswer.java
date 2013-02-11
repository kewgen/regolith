package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;

/**
 * User: mikhail v. kutuzov
 * Date: 17.08.12
 * Time: 12:38
 */
public class ServerTackleGround2BoxAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private short xBox;
    private short yBox;
    private short xGround;
    private short yGround;
    private boolean success;

    public ServerTackleGround2BoxAnswer(MicroByteBuffer buffer, short xBox, short yBox, short xGround, short yGround) {
        this.buffer = buffer;
        this.xBox = xBox;
        this.yBox = yBox;
        this.xGround = xGround;
        this.yGround = yGround;
    }

    @Override
    public short getType() {
        return Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BOX;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(xGround, buffer);
        SimpleSerializer.serialize(yGround, buffer);
        SimpleSerializer.serialize(xBox, buffer);
        SimpleSerializer.serialize(yBox, buffer);
    }
}

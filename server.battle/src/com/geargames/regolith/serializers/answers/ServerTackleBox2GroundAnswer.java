package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 * Date: 17.08.12
 * Time: 14:24
 */
public class ServerTackleBox2GroundAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private short xBox;
    private short yBox;
    private short xGround;
    private short yGround;
    private StateTackle tackle;
    private boolean success;

    public static ServerTackleBox2GroundAnswer answerFailure(MicroByteBuffer buffer){
        return new ServerTackleBox2GroundAnswer(buffer, (short)-1, (short)-1, (short)-1, (short)-1, null, true);
    }

    public static ServerTackleBox2GroundAnswer answerSuccess(MicroByteBuffer buffer, short xBox, short yBox, short xGround, short yGround, StateTackle tackle){
        return new ServerTackleBox2GroundAnswer(buffer, xBox, yBox, xGround, yGround, tackle, true);
    }

    private ServerTackleBox2GroundAnswer(MicroByteBuffer buffer, short xBox, short yBox, short xGround, short yGround, StateTackle tackle, boolean success) {
        this.buffer = buffer;
        this.xBox = xBox;
        this.yBox = yBox;
        this.xGround = xGround;
        this.yGround = yGround;
        this.tackle = tackle;
        this.success = success;
    }

    @Override
    public short getType() {
        return Packets.TAKE_TACKLE_FROM_BOX_PUT_ON_GROUND;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SimpleSerializer.serialize(xBox, buffer);
            SimpleSerializer.serialize(yBox, buffer);
            SimpleSerializer.serialize(xGround, buffer);
            SimpleSerializer.serialize(yGround, buffer);
            SimpleSerializer.serializeEntityReference(tackle, buffer);
        }
    }
}

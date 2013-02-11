package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 * Date: 16.08.12
 * Time: 21:01
 */
public class ServerStateTackleGround2GroundAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private StateTackle stateTackle;
    private short x1;
    private short y1;
    private short x2;
    private short y2;
    private boolean success;

    public static ServerStateTackleGround2GroundAnswer answerFailure(MicroByteBuffer buffer){
        return new ServerStateTackleGround2GroundAnswer(buffer,null,(short)-1,(short)-1,(short)-1,(short)-1,false);
    }

    public static ServerStateTackleGround2GroundAnswer answerSuccess(MicroByteBuffer buffer, StateTackle stateTackle, short x1, short y1, short x2, short y2){
        return new ServerStateTackleGround2GroundAnswer(buffer,stateTackle,x1,y1,x2,y2,true);
    }

    private ServerStateTackleGround2GroundAnswer(MicroByteBuffer buffer, StateTackle stateTackle, short x1, short y1, short x2, short y2, boolean success) {
        this.buffer = buffer;
        this.stateTackle = stateTackle;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.success = success;
    }

    @Override
    public short getType() {
        return Packets.TAKE_TACKLE_FROM_GROUND_PUT_ON_GROUND;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(success, buffer);
        if (success) {
            SimpleSerializer.serializeEntityReference(stateTackle, buffer);
            SimpleSerializer.serialize(x1, buffer);
            SimpleSerializer.serialize(y1, buffer);
            SimpleSerializer.serialize(x2, buffer);
            SimpleSerializer.serialize(y2, buffer);
        }
    }
}

package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public class ServerConfirmationAnswer extends SerializedMessage {
    private short type;
    private boolean confirm;
    private MicroByteBuffer buffer;

    public static ServerConfirmationAnswer answerSuccess(MicroByteBuffer buffer, short type){
        return new ServerConfirmationAnswer(buffer, type, true);
    }

    public static ServerConfirmationAnswer answerFailure(MicroByteBuffer buffer, short type){
        return new ServerConfirmationAnswer(buffer, type, false);
    }

    private ServerConfirmationAnswer(MicroByteBuffer buffer, short type, boolean confirm) {
        this.type = type;
        this.confirm = confirm;
        this.buffer = buffer;
    }

    public short getType() {
        return type;
    }

    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    public void serialize(MicroByteBuffer buffer) {
        SimpleSerializer.serialize(confirm, buffer);
    }
}

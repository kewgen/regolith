package com.geargames.regolith.serializers;


/**
 * User: mkutuzov
 * Date: 04.07.12
 */
public abstract class ClientDeSerializedMessage extends DeSerializedMessage {
    private MicroByteBuffer buffer;

    public boolean ready() {
        return getBuffer() != null;
    }

    public MicroByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(MicroByteBuffer buffer) {
        this.buffer = buffer;
    }

}

package com.geargames.regolith.serializers;

/**
 * User: mkutuzov
 * Date: 04.07.12
 */
public abstract class DeSerializedMessage {
    protected abstract void deSerialize(MicroByteBuffer buffer);

    public abstract MicroByteBuffer getBuffer();

    public void deSerialize() {
        deSerialize(getBuffer());
    }
}

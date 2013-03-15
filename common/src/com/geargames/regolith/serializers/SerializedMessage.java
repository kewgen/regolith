package com.geargames.regolith.serializers;

import com.geargames.regolith.Packets;

import java.util.Arrays;

/**
 * User: mkutuzov
 * Date: 19.06.12
 */
public abstract class SerializedMessage {
    /**
     * Возвращает тип сообщения.
     * @return
     */
    public abstract short getType();

    /**
     * Каждое сообщение сериализуется в этот байтовый буфер.
     * @return
     */
    protected abstract MicroByteBuffer getBuffer();

    /**
     * В этом методе сериализуется содержимое сообщения.
     * @param buffer
     */
    public abstract void serialize(MicroByteBuffer buffer);

    /**
     * Метод сериализации сообщения, опирается на то, что все сообщения имеют общую
     * структуру заголовка, но разное содержимое.
     * @return
     */
    public byte[] serialize(){
        MicroByteBuffer buffer = getBuffer();
        buffer.clear();
        buffer.position(Packets.HEAD_SIZE);
        serialize(buffer);
        buffer.flip();
        SimpleSerializer.serialize((short) (buffer.limit() - Packets.HEAD_SIZE), buffer);
        SimpleSerializer.serialize(getType(), buffer);
        //TODO: либо переделать на цикл либо абстрактный копирователь в gg.common
        return Arrays.copyOf(buffer.getBytes(), buffer.limit());
    }

}

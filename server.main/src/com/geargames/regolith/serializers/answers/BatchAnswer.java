package com.geargames.regolith.serializers.answers;

import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.SimpleSerializer;

import java.util.LinkedList;

/**
 * User: mikhail v. kutuzov
 * Date: 10.01.13
 * Time: 9:55
 */
public class BatchAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private LinkedList<SerializedMessage> messages;

    public BatchAnswer(MicroByteBuffer buffer, LinkedList<SerializedMessage> messages) {
        this.buffer = buffer;
        this.messages = messages;
    }

    @Override
    public short getType() {
        return Packets.BATCH_MESSAGE;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        if (messages.size() != 0) {
            for (SerializedMessage message : messages) {
                int position = buffer.position();
                buffer.position(position + Packets.HEAD_SIZE);
                message.serialize(buffer);
                int newPosition = buffer.position();
                buffer.position(position);
                SimpleSerializer.serialize((short) (newPosition - position - Packets.HEAD_SIZE), buffer);
                SimpleSerializer.serialize(message.getType(), buffer);
                buffer.position(newPosition);
            }
        }
    }

}

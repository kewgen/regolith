package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.Packets;

import java.util.LinkedList;

/**
 * User: mikhail v. kutuzov
 * Date: 10.01.13
 * Time: 9:55
 */
public class BatchAnswer extends SerializedMessage {
    private MicroByteBuffer buffer;
    private LinkedList<SerializedMessage> messages;
    private short type;

    public BatchAnswer() {
    }

    public void setBuffer(MicroByteBuffer buffer) {
        this.buffer = buffer;
    }

    public void setMessages(LinkedList<SerializedMessage> messages) {
        this.messages = messages;
    }

    @Override
    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    @Override
    protected MicroByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void serialize(MicroByteBuffer buffer) {
        if (messages.size() != 0) {
            SimpleSerializer.serialize(messages.size(), buffer);
            for (SerializedMessage message : messages) {
                int position = buffer.getPosition();
                buffer.setPosition(position + Packets.HEAD_SIZE);
                message.serialize(buffer);
                int newPosition = buffer.getPosition();
                buffer.setPosition(position);
                SimpleSerializer.serialize((short) (newPosition - position - Packets.HEAD_SIZE), buffer);
                SimpleSerializer.serialize(message.getType(), buffer);
                buffer.setPosition(newPosition);
            }
        }
    }

}

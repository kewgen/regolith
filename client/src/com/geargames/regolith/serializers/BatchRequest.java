package com.geargames.regolith.serializers;

import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.serializers.requests.ClientSerializedMessage;

/**
 * User: mikhail v. kutuzov
 * Отправить сообщения скопом.
 * Класс работает на допущении, что пользователь не будет отправлять через него вложенные BatchRequest.
 */
public class BatchRequest extends ClientSerializedMessage {
    private ArrayList requests;

    public BatchRequest(ClientConfiguration configuration) {
        super(configuration);
    }

    public ArrayList getRequests() {
        return requests;
    }

    public void setRequests(ArrayList requests) {
        this.requests = requests;
    }

    public short getType() {
        return Packets.BATCH_MESSAGE;
    }

    public void serialize(MicroByteBuffer buffer) {
        for (int i = 0; i < requests.size(); i++) {
            ClientSerializedMessage message = ((ClientSerializedMessage) requests.get(i));
            int position = buffer.getPosition();
            buffer.setPosition(position + Packets.HEAD_SIZE);
            message.serialize(buffer);
            int newPosition = buffer.getPosition();
            buffer.setPosition(position);
            SimpleSerializer.serialize((short) (newPosition - Packets.HEAD_SIZE - position), buffer);
            SimpleSerializer.serialize(message.getType(), buffer);
            buffer.setPosition(newPosition);
        }
        requests.clear();
    }
}

package com.geargames.regolith.serializers;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.regolith.Packets;

import java.util.ArrayList;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Ответ на связку сообщений возвращает список ответов в том порядке, в котором были отосланы соответствующие запросы.
 */
public abstract class BatchAnswer extends ClientDeSerializedMessage {

    protected abstract ClientDeSerializedMessage getTypedAnswer(int i, short type);

    public abstract ArrayList getAnswers();

    public void deSerialize(MicroByteBuffer buffer) throws Exception {
        int size = SimpleDeserializer.deserializeInt(buffer);
        for (int i = 0; i < size; i++) {
            int position = buffer.getPosition();
            short messageLength = SimpleDeserializer.deserializeShort(buffer);
            short messageType = SimpleDeserializer.deserializeShort(buffer);
            getTypedAnswer(i, messageType).deSerialize(buffer);
            // Проверка того, что сообщение было считано полностью и не считано лишних данных
            int expectedPosition = position + Packets.HEAD_SIZE + messageLength;
            if (buffer.getPosition() != expectedPosition) {
                Debug.error("BatchAnswer.deSerialize(): The expected position does not coincide with the actual position (" +
                        "expected=" + expectedPosition +
                        "; actual=" + buffer.getPosition() +
                        "; index=" + i +
                        "; msg count=" + size +
                        "; msg length=" + messageLength +
                        "; msg type=" + messageType +
                        ")");
            }
            // На всякий случай перемещаем позицию буфера туда, где начинается следующее сообщение
            buffer.setPosition(expectedPosition);
        }
    }

}

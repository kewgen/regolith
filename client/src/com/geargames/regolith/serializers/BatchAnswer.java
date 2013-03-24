package com.geargames.regolith.serializers;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.String;
import com.geargames.regolith.Packets;

/**
 * User: mikhail v. kutuzov, abarakov
 * Ответ на связку сообщений возвращает список ответов в том порядке, в котором были отосланы соответствующие запросы.
 */
public abstract class BatchAnswer extends ClientDeSerializedMessage {

    protected abstract ClientDeSerializedMessage getAnswer(int i, short type);

    public void deSerialize(MicroByteBuffer buffer) throws Exception {

            int size = SimpleDeserializer.deserializeInt(buffer);
            for (int i = 0; i < size; i++) {
                int position = buffer.position();
                short messageLength = SimpleDeserializer.deserializeShort(buffer);
                short messageType = SimpleDeserializer.deserializeShort(buffer);
                getAnswer(i, messageType).deSerialize(buffer);
                // Проверка того, что сообщение было считано полностью и не считано лишних данных
                int expectedPosition = position + Packets.HEAD_SIZE + messageLength;
                if (buffer.position() != expectedPosition) {
                    Debug.error(
                            String.valueOfC("BatchAnswer.deSerialize(): The expected position does not coincide with the actual position (").
                                    concatC("expected=").concatI(expectedPosition).
                                    concatC("; actual=").concatI(buffer.position()).
                                    concatC("; index=").concatI(i).
                                    concatC("; msg count=").concatI(size).
                                    concatC("; msg length=").concatI(messageLength).
                                    concatC("; msg type=").concatI(messageType).
                                    concatC(")"));
                }
                // На всякий случай перемещаем позицию буфера туда, где начинается следующее сообщение
                buffer.position(expectedPosition);
            }
    }

}

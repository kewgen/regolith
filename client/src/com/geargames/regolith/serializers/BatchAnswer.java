package com.geargames.regolith.serializers;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.util.ArrayList;
import com.geargames.common.String;
import com.geargames.regolith.Packets;

/**
 * User: mikhail v. kutuzov, abarakov
 * Ответ на связку сообщений возвращает список ответов в том порядке, в котором были отосланы соответствующие запросы.
 */
public class BatchAnswer extends ClientDeSerializedMessage {
    private ArrayList answers;

    public BatchAnswer() {
    }

    public ArrayList getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList answers) {
        this.answers = answers;
    }

    public void deSerialize(MicroByteBuffer buffer) {
        if (answers != null) {
            for (int i = 0; i < answers.size(); i++) {
                ClientDeSerializedMessage answer = (ClientDeSerializedMessage) answers.get(i);
                int position = buffer.position();
                // buffer.skip(Packets.HEAD_SIZE);
                short messageLength = SimpleDeserializer.deserializeShort(buffer);
                short messageType = SimpleDeserializer.deserializeShort(buffer);

                answer.deSerialize(buffer);
                // Проверка того, что сообщение было считано полностью и не считано лишних данных
                int expectedPosition = position + Packets.HEAD_SIZE + messageLength;
                if (buffer.position() != expectedPosition) {
                    Debug.error(
                            String.valueOfC("BatchAnswer.deSerialize(): The expected position does not coincide with the actual position (").
                                    concatC("expected=").concatI(expectedPosition).
                                    concatC("; actual=").concatI(buffer.position()).
                                    concatC("; index=").concatI(i).
                                    concatC("; msg count=").concatI(answers.size()).
                                    concatC("; msg length=").concatI(messageLength).
                                    concatC("; msg type=").concatI(messageType).
                                    concatC(")"));
                }
                // На всякий случай перемещаем позицию буфера туда, где начинается следующее сообщение
                buffer.position(expectedPosition);
            }
        }
    }

}

package com.geargames.regolith.serializers;

import com.geargames.common.util.ArrayList;
import com.geargames.regolith.Packets;

/**
 * User: mikhail v. kutuzov
 * Ответ на связку сообщений возвращает список ответов в том порядке в котором были вызваны запросы.
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

    protected void deSerialize(MicroByteBuffer buffer) {
        if (answers != null) {
            for (int i = 0; i < answers.size(); i++) {
                ClientDeSerializedMessage answer = (ClientDeSerializedMessage) answers.get(i);
//                answer.deSerializeAndCheckHead(buffer) {
//                    buffer.skip(Packets.HEAD_SIZE);
                    /*short length = */SimpleDeserializer.deserializeShort(buffer);
//                    if (length != answer.getRequiredLength()) {
//                        // error
//                    }
                    /*short type = */SimpleDeserializer.deserializeShort(buffer);
//                    if (type != answer.getRequiredType()) {
//                        // error
//                    }
//                }
                answer.deSerialize(buffer);
            }
        }
    }
}

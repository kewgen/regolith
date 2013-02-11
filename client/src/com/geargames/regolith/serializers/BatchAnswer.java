package com.geargames.regolith.serializers;

import com.geargames.common.util.ArrayList;

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
                ((ClientDeSerializedMessage) answers.get(i)).deSerialize(buffer);
            }
        }
    }
}

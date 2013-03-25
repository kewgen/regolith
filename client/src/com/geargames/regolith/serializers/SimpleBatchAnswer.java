package com.geargames.regolith.serializers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.util.ArrayList;

/**
 * User: m.v.kutuzov
 * Date: 24.03.13
 */
public class SimpleBatchAnswer extends  BatchAnswer {
    private ArrayList answers;

    public ArrayList getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList answers) {
        this.answers = answers;
    }

    protected ClientDeSerializedMessage getTypedAnswer(int i, short type) {
        return (ClientDeSerializedMessage)answers.get(i);
    }
}

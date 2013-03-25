package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.serializers.BatchAnswer;


/**
 * User: mkutuzov
 * Date: 06.07.12
 */
public class ClientBrowseBattlesAnswer extends BatchAnswer {

    private ArrayList answers;

    public ClientBrowseBattlesAnswer() {
        answers = new ArrayList();
    }

    protected ClientDeSerializedMessage getTypedAnswer(int i, short type) {
        ClientListenToBattleAnswer answer = new ClientListenToBattleAnswer();
        answers.add(answer);
        return answer;
    }

    public ArrayList getAnswers() {
        return answers;
    }
}

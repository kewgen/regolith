package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.serializers.BatchAnswer;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

import java.util.Vector;


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

    /**
     * Получить список объектов типа ClientListenToBattleAnswer.
     */
    public ArrayList getAnswers() {
        return answers;
    }

    public ClientBattleCollection getBattles() {
        ClientBattleCollection battles = new ClientBattleCollection();
        battles.setBattles(new Vector(answers.size()));
        for (int i = 0 ; i < answers.size(); i++) {
            battles.add(((ClientListenToBattleAnswer) answers.get(i)).getBattle());
        }
        return battles;
    }

}

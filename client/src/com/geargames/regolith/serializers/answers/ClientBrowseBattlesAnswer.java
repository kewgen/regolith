package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.Packets;
import com.geargames.regolith.application.ObjectManager;
import com.geargames.regolith.serializers.BatchAnswer;
import com.geargames.regolith.units.dictionaries.ClientBattleCollection;

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
        ClientDeSerializedMessage answer;
        switch (type) {
            case Packets.LISTEN_TO_CREATED_BATTLE:
                answer = new ClientListenToBattleAnswer();
                answers.add(answer);
                break;
            case Packets.LISTEN_TO_UPDATED_BATTLE:
                answer = new ClientListenToUpdatedBattles();
                break;
            case Packets.LISTEN_TO_DELETED_BATTLE:
                answer = new ClientListenToDeletedBattle();
                break;
            default:
                //todo потенциальный NPE
                answer = null;
        }
        return answer;
    }

    /**
     * Получить список объектов типа ClientListenToBattleAnswer.
     */
    public ArrayList getAnswers() {
        return answers;
    }

    public void deSerialize() throws Exception {
        super.deSerialize();
        ClientBattleCollection battles = ObjectManager.getInstance().getBattleCollection();
        for (int i = 0; i < answers.size(); i++) {
            battles.add(((ClientListenToBattleAnswer) answers.get(i)).getBattle());
        }
    }

    public ClientBattleCollection getBattles() {
        return ObjectManager.getInstance().getBattleCollection();
    }

}

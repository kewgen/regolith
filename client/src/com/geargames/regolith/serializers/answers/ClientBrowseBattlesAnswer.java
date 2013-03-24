package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.regolith.serializers.BatchAnswer;

/**
 * User: mkutuzov
 * Date: 06.07.12
 */
public class ClientBrowseBattlesAnswer extends BatchAnswer {

    protected ClientDeSerializedMessage getAnswer(int i, short type) {
        return new ClientCreateBattleAnswer();
    }
}

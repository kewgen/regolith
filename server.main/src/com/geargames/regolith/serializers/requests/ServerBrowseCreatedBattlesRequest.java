package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.RegolithException;
import com.geargames.regolith.managers.ServerBattleMarketManager;
import com.geargames.regolith.serializers.MicroByteBuffer;
import com.geargames.regolith.serializers.SerializedMessage;
import com.geargames.regolith.serializers.answers.ServerBrowseCreatedBattlesAnswer;
import com.geargames.regolith.service.Client;

/**
 * User: mkutuzov
 * Date: 13.07.12
 */
public class ServerBrowseCreatedBattlesRequest extends MainOneToClientRequest {
    private ServerBattleMarketManager battleMarketManager;

    public ServerBrowseCreatedBattlesRequest(ServerBattleMarketManager battleMarketManager) {
        this.battleMarketManager = battleMarketManager;
    }

    @Override
    public SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        return new ServerBrowseCreatedBattlesAnswer(writeBuffer, battleMarketManager.battlesJoinTo());
    }
}
